package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static com.github.pedramrn.slick.parent.util.FileUtils.readFile;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */
public class ApiTmdbMockTest {

    private ApiTmdbMock apiTmdbMock;

    @Before
    public void setUp() throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();

        final InputStream inputStreamTmdb = ClassLoader.getSystemResourceAsStream("api_tmdb_movie_list.json");
        final String bufferTmdb = readFile(inputStreamTmdb);
        Type type = new TypeToken<List<MovieTmdb>>() {}.getType();

        List<MovieTmdb> movieTmdbList = gson.fromJson(bufferTmdb, type);

        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_trakt_trending_200.json");
        final String bufferTrakt = readFile(inputStreamTrakt);
        Type typeTrakt = new TypeToken<List<MovieTraktPageMetadata>>() {}.getType();

        List<MovieTraktPageMetadata> trendingList = gson.fromJson(bufferTrakt, typeTrakt);

        this.apiTmdbMock = new ApiTmdbMock(gson, movieTmdbList, trendingList);

    }

    @Test
    public void movie() throws Exception {
        apiTmdbMock.movie(297762).test().assertValueCount(1).assertComplete();
        apiTmdbMock.movie(11111111).test().assertValueCount(1).assertValue(new Predicate<MovieTmdb>() {
            @Override
            public boolean test(@NonNull MovieTmdb movieTmdb) throws Exception {
                return movieTmdb.id().equals(293167);
            }
        });
    }

}