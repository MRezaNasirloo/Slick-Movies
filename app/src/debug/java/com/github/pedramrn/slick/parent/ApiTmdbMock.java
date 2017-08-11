package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MoviePageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonPageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdbResults;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.NetworkBehavior;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */

public class ApiTmdbMock extends ApiMockBase<ApiTmdb> implements ApiTmdb {

    private final List<MovieTmdb> popularList;

    public ApiTmdbMock(BehaviorDelegate<ApiTmdb> delegate, Gson gson, List<MovieTmdb> popularList) {
        super(delegate, gson);
        this.popularList = popularList;
    }

    public ApiTmdbMock(NetworkBehavior behavior, Gson gson, List<MovieTmdb> popularList) {
        super(behavior, gson);
        this.popularList = popularList;
    }

    public ApiTmdbMock(Gson gson, List<MovieTmdb> popularList) {
        super(gson);
        this.popularList = popularList;
    }

    @Override
    public Observable<MovieTmdb> movieFull(@Path("movie_id") Integer id) {
        return Observable.never();
    }

    @Override
    public Observable<MovieTmdb> movie(@Path("movie_id") final Integer id) {
        return Observable.fromIterable(popularList).filter(new Predicate<MovieTmdb>() {
            @Override
            public boolean test(@NonNull MovieTmdb movieTmdb) throws Exception {
                return movieTmdb.id().equals(id);
            }
        }).first(popularList.get(0)).toObservable();
    }

    @Override
    public Observable<MoviePageTmdb> similar(@Path("movie_id") Integer id, @Query("page") int page) {
        return Observable.never();
    }

    @Override
    public Observable<MoviePageTmdb> upcoming(@Query("page") int page) {
        return Observable.never();
    }

    @Override
    public Observable<MoviePageTmdb> nowPlaying(@Query("page") int page) {
        return Observable.never();
    }

    @Override
    public Observable<MovieTmdb> withVideos(@Path("movie_id") Integer id) {
        return Observable.never();
    }

    @Override
    public Observable<VideoTmdbResults> justVideos(@Path("movie_id") Integer id) {
        return Observable.never();
    }

    @Override
    public Observable<MoviePageTmdb> searchMovie(@Query("query") String query, @Query("page") int page) {
        return Observable.never();
    }

    @Override
    public Observable<PersonPageTmdb> searchPerson(@Query("query") String query, @Query("page") int page) {
        return Observable.never();
    }

    @Override
    protected Class<ApiTmdb> getApiClassType() {
        return ApiTmdb.class;
    }
}
