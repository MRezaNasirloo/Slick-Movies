package com.github.pedramrn.slick.parent.ui.search;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.interceptor.InterceptorAuthToken;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleNetworkBase;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.search.router.RouterSearchImpl;
import com.github.pedramrn.slick.parent.ui.search.state.ViewStateSearch;
import com.github.pedramrn.slick.parent.util.FileUtils;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
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
        presenterSearch = new PresenterSearch(routerSearch, new MapperMovieSmallDomainMovieSmall(), Schedulers.trampoline(), Schedulers.trampoline());


    }

    @Test
    public void query() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(jsonMovies)
        );

        server.enqueue(new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(jsonMovies)
        );


        TestObserver<ViewStateSearch> test = presenterSearch.updateStream().test();
        PublishSubject<String> queryNewText = PublishSubject.<String>create();
        presenterSearch.query(queryNewText, openClose);
        queryNewText.onNext("star");
        test.awaitCount(2).assertNoErrors().assertValueCount(2);
        queryNewText.onNext("star wars");
        test.assertNoErrors().assertNotComplete().assertValueCount(3);
    }

}