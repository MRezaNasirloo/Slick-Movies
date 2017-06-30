package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.NoSuchElementException;

import static com.github.pedramrn.slick.parent.TestUtils.readFile;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */
public class ApiTmdbMockTest {

    private ApiTmdbMock apiTmdbMock;

    @Before
    public void setUp() throws Exception {
        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_tmdb_popular_120.json");
        final String bufferTrakt = readFile(inputStreamTrakt);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();

        apiTmdbMock = new ApiTmdbMock(bufferTrakt, gson);

    }

    @Test
    public void movie() throws Exception {
        apiTmdbMock.movie(297762).test().assertValueCount(1).assertComplete();
        apiTmdbMock.movie(11111111).test().assertValueCount(0).assertError(NoSuchElementException.class);
    }

}