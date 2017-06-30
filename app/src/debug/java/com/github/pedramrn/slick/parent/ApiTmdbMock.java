package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdbResults;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.TraktPageMetadata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.NotImplementedException;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import retrofit2.http.Path;
import retrofit2.mock.BehaviorDelegate;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */

public class ApiTmdbMock extends ApiMockBase<ApiTmdb> implements ApiTmdb {

    private final Type type = new TypeToken<List<TraktPageMetadata>>() {
    }.getType();

    private List<MovieTmdb> popularList;

    public ApiTmdbMock(BehaviorDelegate<ApiTmdb> delegate, String jsonData, Gson gson) {
        super(delegate, jsonData, gson);
    }

    public ApiTmdbMock(String jsonData, Gson gson) {
        super(jsonData, gson);

    }

    @Override
    public Observable<MovieTmdb> movieFull(@Path("movie_id") Integer id) {
        throw new NotImplementedException(new Throwable());
    }

    @Override
    public Observable<MovieTmdb> movie(@Path("movie_id") final Integer id) {
        return Observable.fromIterable(popularList).filter(new Predicate<MovieTmdb>() {
            @Override
            public boolean test(@NonNull MovieTmdb movieTmdb) throws Exception {
                return movieTmdb.id().equals(id);
            }
        }).firstOrError().toObservable();
    }

    @Override
    public Observable<MovieTmdb> withVideos(@Path("movie_id") Integer id) {
        throw new NotImplementedException(new Throwable());
    }

    @Override
    public Observable<VideoTmdbResults> justVideos(@Path("movie_id") Integer id) {
        throw new NotImplementedException(new Throwable());
    }

    @Override
    protected Class<ApiTmdb> getApiClassType() {
        return ApiTmdb.class;
    }

    @Override
    protected void init(String jsonData, Gson gson) {
        Type type = new TypeToken<List<TraktPageMetadata>>() {
        }.getType();
        Type type2 = new TypeToken<List<MovieTmdb>>() {
        }.getType();
        popularList = gson.fromJson(jsonData, type2);
    }
}
