package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MoviePageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonPageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdbResults;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.mock.NetworkBehavior;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */

public class ApiTmdbMock extends ApiMockBase<ApiTmdb> implements ApiTmdb {

    private final List<MovieTmdb> popularList;
    private final List<MovieTraktPageMetadata> trendingList;

    public ApiTmdbMock(
            NetworkBehavior behavior,
            Gson gson,
            List<MovieTmdb> popularList,
            List<MovieTraktPageMetadata> trendingList
    ) {
        super(behavior, gson);
        this.popularList = popularList;
        this.trendingList = trendingList;
    }

    public ApiTmdbMock(Gson gson, List<MovieTmdb> popularList, List<MovieTraktPageMetadata> trendingList) {
        super(gson);
        this.popularList = popularList;
        this.trendingList = trendingList;
    }

    @Override
    public Observable<MovieTmdb> movieFull(@Path("movie_id") Integer id) {
        return Observable.never();
    }

    @Override
    public Observable<MovieTmdb> movie(@Path("movie_id") final Integer id) {
        MovieTraktMetadata metadata = Observable.fromIterable(trendingList)
                .filter(new Predicate<MovieTraktPageMetadata>() {
                    @Override
                    public boolean test(@NonNull MovieTraktPageMetadata movieTraktPageMetadata) throws Exception {
                        return id.equals(movieTraktPageMetadata.movie().ids().tmdb());
                    }
                })
                .blockingFirst().movie();
        MovieTmdb movieTmdb = popularList.get(0)
                .toBuilder()
                .id(metadata.ids().tmdb())
                .title(metadata.title())
                .releaseDate(metadata.year() + "-01-01")
                .build();
        return Observable.fromIterable(popularList).filter(new Predicate<MovieTmdb>() {
            @Override
            public boolean test(@NonNull MovieTmdb movieTmdb) throws Exception {
                return movieTmdb.id().equals(id);
            }
        }).first(movieTmdb).toObservable();
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
    public Observable<PersonTmdb> personDetails(@Path("id") int id) {
        return Observable.never();
    }

    @Override
    public Observable<PersonTmdb> personDetailsWithCredits(@Path("id") int id) {
        return Observable.never();
    }

    @Override
    protected Class<ApiTmdb> getApiClassType() {
        return ApiTmdb.class;
    }
}
