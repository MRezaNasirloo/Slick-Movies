package com.github.pedramrn.slick.parent.ui.details.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.interceptor.InterceptorAuthToken;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleNetworkBase;
import com.github.pedramrn.slick.parent.domain.model.CommentDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.util.FileUtils;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */
public class RouterCommentsImplTest {

    @Rule public TemporaryFolder folder = new TemporaryFolder();

    private RouterCommentsImpl routerComments;

    private MockWebServer server;
    private String jsonOk;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        HttpUrl httpUrl = server.url("/");
        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("comments_trakt.json");
        jsonOk = FileUtils.readFile(inputStreamTrakt);
        File file = folder.newFile();
        file.deleteOnExit();
        ModuleNetworkBase moduleNetwork = new ModuleNetwork();
        Gson gson = moduleNetwork.baseGsonConverterFactory();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ApiTrakt apiTrakt = moduleNetwork.baseApiTrakt(httpUrl,
                moduleNetwork.baseOkHttpClient(
                        Arrays.asList(loggingInterceptor, new InterceptorAuthToken()),
                        Collections.<Interceptor>emptyList(),
                        new Cache(file, 10 * 1024)),
                moduleNetwork.baseRetrofit(gson),
                gson);
        routerComments = new RouterCommentsImpl(apiTrakt);

    }

    @Test
    public void comments() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(HttpsURLConnection.HTTP_OK)
                .setBody(jsonOk)
                .setHeader("x-pagination-item-count", 35)
                .setHeader("x-pagination-limit", 10)
                .setHeader("x-pagination-page-count", 4)
                .setHeader("x-pagination-page", 1)
        );
        routerComments.comments("tt2250912").flatMap(new Function<PagedDomain<CommentDomain>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(@NonNull PagedDomain<CommentDomain> com) throws Exception {
                return Observable.fromArray(com.count(), com.page(), com.pages());
            }
        })
                .test()
                .assertValues(35, 1, 4)
                .assertNoErrors()
                .assertComplete();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
        folder.delete();
    }
}
