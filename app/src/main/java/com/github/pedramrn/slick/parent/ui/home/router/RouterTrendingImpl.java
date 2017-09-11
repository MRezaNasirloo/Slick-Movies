package com.github.pedramrn.slick.parent.ui.home.router;

import android.support.annotation.IntRange;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadataImpl;
import com.github.pedramrn.slick.parent.domain.router.RouterTrending;
import com.github.pedramrn.slick.parent.domain.rx.PassThroughMap;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-23
 */

public class RouterTrendingImpl implements RouterTrending {

    private final ApiTrakt apiTrakt;
    private final ApiTmdb apiTmdb;
    private final MapperMovie mapper;

    @Inject
    public RouterTrendingImpl(ApiTrakt apiTrakt, ApiTmdb apiTmdb, MapperMovie mapper) {
        this.apiTrakt = apiTrakt;
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<MovieMetadata> trending() {
        return trending(1, 10);
    }

    @Override
    public Observable<MovieMetadata> trending(@IntRange(from = 1, to = Long.MAX_VALUE) int page, int size) {
        if (page < 1) { throw new IllegalArgumentException("Page should be a positive number"); }
        if (size < 1) { throw new IllegalArgumentException("Size should be a positive number"); }
        return apiTrakt.trending(page, size)
                .concatMap(new Function<List<MovieTraktPageMetadata>, ObservableSource<MovieTraktPageMetadata>>() {
                    @Override
                    public ObservableSource<MovieTraktPageMetadata> apply(@NonNull List<MovieTraktPageMetadata> mtp)
                            throws Exception {
                        return Observable.fromIterable(mtp);
                    }
                })
                .map(new Function<MovieTraktPageMetadata, MovieMetadata>() {
                    @Override
                    public MovieMetadata apply(@NonNull MovieTraktPageMetadata mtm) throws Exception {
                        MovieTraktMetadata movie = mtm.movie();
                        return MovieMetadataImpl.create(movie.ids().tmdb(), movie.ids().imdb(), movie.title(), movie.year());
                    }
                })
                .lift(new PassThroughMap<MovieMetadata>() {
                    @Override
                    public Observable<MovieMetadata> apply(@NonNull MovieMetadata movieMetadata) throws Exception {
                        return apiTmdb.movie(movieMetadata.id()).map(mapper).cast(MovieMetadata.class);
                    }
                });
    }

    /*@Override
    public Observable<MovieMetadata> trending(@IntRange(from = 1, to = Long.MAX_VALUE) int page, int size) {
        if (page < 1) { throw new IllegalArgumentException("Page should be a positive number"); }
        if (size < 1) { throw new IllegalArgumentException("Size should be a positive number"); }
        return apiTrakt.trending(page, size)
                .concatMap(new Function<List<MovieTraktPageMetadata>, ObservableSource<MovieTraktPageMetadata>>() {
                    @Override
                    public ObservableSource<MovieTraktPageMetadata> apply(@NonNull List<MovieTraktPageMetadata> mtp)
                            throws Exception {
                        return Observable.fromIterable(mtp);
                    }
                })
                .map(new Function<MovieTraktPageMetadata, MovieMetadata>() {
                    @Override
                    public MovieMetadata apply(@NonNull MovieTraktPageMetadata mtm) throws Exception {
                        MovieTraktMetadata movie = mtm.movie();
                        return MovieMetadataImpl.create(movie.ids().tmdb(), movie.ids().imdb(), movie.title(), movie.year());
                    }
                });
    }*/

    @Override
    public Observable<MovieDomain> trending(Integer... ids) {
        return Observable.fromArray(ids)
                .concatMap(new Function<Integer, ObservableSource<MovieTmdb>>() {
                    @Override
                    public ObservableSource<MovieTmdb> apply(@NonNull Integer id) throws Exception {
                        return apiTmdb.movie(id);
                    }
                })
                .map(mapper);
    }

}
