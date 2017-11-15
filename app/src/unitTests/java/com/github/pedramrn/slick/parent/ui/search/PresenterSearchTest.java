package com.github.pedramrn.slick.parent.ui.search;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.interceptor.InterceptorAuthToken;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleNetworkBase;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.search.router.RouterSearchImpl;
import com.github.pedramrn.slick.parent.util.FileUtils;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */
public class PresenterSearchTest {

    @Rule public TemporaryFolder folder = new TemporaryFolder();

    private RouterSearchImpl routerSearch;

    private MockWebServer server;
    private String jsonMovies;
    private String jsonPersons;
    private PresenterSearch presenterSearch;
    private TestScheduler testScheduler;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        HttpUrl httpUrl = server.url("/");
        final InputStream inputStreamSearchMovie = ClassLoader.getSystemResourceAsStream("search_movie.json");
        final InputStream inputStreamSearchPerson = ClassLoader.getSystemResourceAsStream("search_person.json");
        jsonMovies = FileUtils.readFile(inputStreamSearchMovie);
        jsonPersons = FileUtils.readFile(inputStreamSearchPerson);
        File file = folder.newFile();
        file.deleteOnExit();
        ModuleNetworkBase moduleNetwork = new ModuleNetwork();
        Gson gson = moduleNetwork.baseGsonConverterFactory();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ApiTmdb apiTmdb = moduleNetwork.baseApiTmdb(httpUrl,
                moduleNetwork.baseOkHttpClient(
                        Arrays.asList(loggingInterceptor, new InterceptorAuthToken()),
                        Collections.<Interceptor>emptyList(),
                        new Cache(file, 10 * 1024)),
                moduleNetwork.baseRetrofit(gson),
                gson);
        routerSearch = new RouterSearchImpl(apiTmdb, new MapperMovieSmall());
        testScheduler = new TestScheduler();
        presenterSearch = new PresenterSearch(routerSearch, new MapperMovieSmallDomainMovieSmall(), testScheduler, testScheduler);
    }

    @Test
    public void query() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(jsonMovies)
        );

        server.enqueue(new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(jsonMovies)
        );

        PublishSubject<String> queryNewText = PublishSubject.create();

        ViewSearch viewSearch = Mockito.mock(ViewSearch.class);
        Mockito.when(viewSearch.queryNexText()).thenReturn(queryNewText);
        Mockito.when(viewSearch.searchOpenClose()).thenReturn(Observable.never());

        presenterSearch.onViewUp(viewSearch);
        queryNewText.onNext("star");
        testScheduler.advanceTimeTo(5, TimeUnit.SECONDS);

        Mockito.verify(viewSearch, Mockito.times(2)).update(Mockito.argThat(argument -> argument.size() == 20));
    }

    @Test
    public void testOnErrorReturn() {
        final Observable<String> stringObservable = Observable.defer(() -> {
            throw new RuntimeException("Too Bad");
        });
        Observable<String> observable = Observable.intervalRange(0, 5, 0, 500, TimeUnit.MILLISECONDS)
                .flatMap(aLong -> stringObservable.onErrorReturn(throwable -> "That Passed early"))
                .onErrorReturn(throwable -> "That Passed")
                .doOnComplete(() -> System.out.println("OnComplete first stream."));

        observable.mergeWith(Observable.<String>never())
                .doOnNext(s -> System.out.println("accept() called with: s = [" + s + "]"))
                .test().awaitDone(5, TimeUnit.SECONDS);
    }

}