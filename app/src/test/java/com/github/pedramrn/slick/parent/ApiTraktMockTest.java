package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.TraktPageMetadata;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static com.github.pedramrn.slick.parent.TestUtils.readFile;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */
public class ApiTraktMockTest {

    private ApiTraktMock apiTraktMock;

    @Before
    public void setUp() throws Exception {
        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_trakt.json");
        final String bufferTrakt = readFile(inputStreamTrakt);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();

        apiTraktMock = new ApiTraktMock(bufferTrakt, gson);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void trending() throws Exception {
        List<List<TraktPageMetadata>> values = apiTraktMock.trending(1, 10).test().assertValueCount(1).values();
        Assert.assertEquals(values.get(0).size(), 10);
    }

}