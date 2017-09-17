package com.github.pedramrn.slick.parent.mock;

import com.github.pedramrn.slick.parent.ApiTmdbMock;
import com.github.pedramrn.slick.parent.ApiTraktMock;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.mock.NetworkBehavior;

import static com.github.pedramrn.slick.parent.util.FileUtils.readFile;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class ApiMockProvider {

    private final ApiTmdb apiTmdbMock;
    private final ApiTrakt apiTraktMock;

    public ApiMockProvider() throws IOException {
        final InputStream inputStreamTmdb = ClassLoader.getSystemResourceAsStream("api_tmdb_movie_list.json");
        final String bufferTmdb = readFile(inputStreamTmdb);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();

        NetworkBehavior behavior = new ModuleNetwork().networkBehavior();

        List<MovieTmdb> movieTmdbList = gson.fromJson(bufferTmdb, new TypeToken<List<MovieTmdb>>() {}.getType());

        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_trakt_trending_200.json");
        final String bufferTrakt = readFile(inputStreamTrakt);

        List<MovieTraktPageMetadata> trendingList =
                gson.fromJson(bufferTrakt, new TypeToken<List<MovieTraktPageMetadata>>() {}.getType());
        apiTmdbMock = new ApiTmdbMock(behavior, gson, movieTmdbList, trendingList);
        apiTraktMock = new ApiTraktMock(behavior, gson, trendingList, null);
    }

    public ApiTmdb apiTmdb() {
        return apiTmdbMock;
    }

    public ApiTrakt apiTrakt() {
        return apiTraktMock;
    }

}
