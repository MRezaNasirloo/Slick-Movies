package com.github.pedramrn.slick.parent.ui.search.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.interceptor.InterceptorAuthToken;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleNetworkBase;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
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
public class RouterSearchImplTest {

    @Rule public TemporaryFolder folder = new TemporaryFolder();

    private RouterSearchImpl routerSearch;

    private MockWebServer server;
    private String jsonMovies;
    private String jsonPersons;

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
    }

    @Test
    public void movies() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(jsonMovies)
        );

        routerSearch.movies("star", 1).test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
        ;
    }

    @Test
    public void persons() throws Exception {

        server.enqueue(new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(jsonPersons)
        );

        routerSearch.persons("jenifer", 1).test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
        ;
    }

}