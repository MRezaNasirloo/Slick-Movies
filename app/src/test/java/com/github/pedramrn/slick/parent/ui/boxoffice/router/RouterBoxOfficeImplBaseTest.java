package com.github.pedramrn.slick.parent.ui.boxoffice.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.domain.mapper.MapperSimpleData;

import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static com.github.pedramrn.slick.parent.TestUtils.readFile;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-21
 */

public class RouterBoxOfficeImplBaseTest {

    private MockWebServer server;

    protected RouterBoxOfficeTmdbImpl routerBoxOffice;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // final InputStream inputStreamOmdb = ClassLoader.getSystemResourceAsStream("api_omdb.json");
        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_trakt.json");
        final InputStream inputStreamTmdb = ClassLoader.getSystemResourceAsStream("api_tmdb_simple.json");

        final String bufferTmdb = readFile(inputStreamTmdb);
        final String bufferTrakt = readFile(inputStreamTrakt);

        server = new MockWebServer();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                String path = request.getPath();
                if (path.startsWith("/movies/boxoffice")) {
                    return new MockResponse()
                            .setResponseCode(HTTP_OK)
                            .setBody(bufferTrakt);
                } else if (path.startsWith("/movie/")) {
                    return new MockResponse()
                            .setResponseCode(HTTP_OK)
                            .setBody(bufferTmdb);
                } else {
                    throw new IllegalStateException(path + "is not supported.");
                }
            }
        });
        server.start();
        HttpUrl url = server.url("/");

        ModuleNetwork mn = new ModuleNetwork();
        ApiTrakt apiTrakt = mn.boxOfficeWeekend(url, mn.okHttpClient(), mn.gsonConverterFactory());
        ApiTmdb apiTmdb = mn.tmdbClient(url, mn.okHttpClient(), mn.gsonConverterFactory());

        routerBoxOffice = new RouterBoxOfficeTmdbImpl(apiTrakt, apiTmdb, new MapperSimpleData(), Schedulers.trampoline());
    }

    private static final String TAG = RouterBoxOfficeImplBaseTest.class.getSimpleName();

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
