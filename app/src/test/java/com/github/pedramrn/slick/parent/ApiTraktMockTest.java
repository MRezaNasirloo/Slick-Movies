package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.TraktPageMetadata;
import com.github.pedramrn.slick.parent.util.ListToObserable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static com.github.pedramrn.slick.parent.TestUtils.readFile;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */
public class ApiTraktMockTest {

    private ApiTraktMock apiTraktMock;

    @Before
    public void setUp() throws Exception {
        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_trakt_trending_200.json");
        final String bufferTrakt = readFile(inputStreamTrakt);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();

        Type type = new TypeToken<List<TraktPageMetadata>>() {
        }.getType();

        List<TraktPageMetadata> trendingList = gson.fromJson(bufferTrakt, type);
        apiTraktMock = new ApiTraktMock(gson, trendingList);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void trending() throws Exception {
        apiTraktMock.trending(1, 10)
                .flatMap(new ListToObserable<TraktPageMetadata>())
                .test()
                .assertValueCount(10)
                .assertComplete();

        apiTraktMock.trending(2, 3)
                .flatMap(new ListToObserable<TraktPageMetadata>())
                .test()
                .assertValueCount(3)
                .assertValueAt(0, new Predicate<TraktPageMetadata>() {
                    @Override
                    public boolean test(@NonNull TraktPageMetadata tpm) throws Exception {
                        return tpm.movie().ids().tmdb().equals(417644);
                    }
                })
                .assertComplete();
    }
}