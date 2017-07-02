package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
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
        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_tmdb_movie_list.json");
        final String bufferTrakt = readFile(inputStreamTrakt);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();

        Type type = new TypeToken<List<MovieTmdb>>() {
        }.getType();

        List<MovieTmdb> movieTmdbList = gson.fromJson(bufferTrakt, type);
        apiTmdbMock = new ApiTmdbMock(gson, movieTmdbList);

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