package com.github.pedramrn.slick.parent.ui.home.router;

import android.support.annotation.IntRange;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadataImpl;
import com.github.pedramrn.slick.parent.domain.router.RouterPopular;
import com.github.pedramrn.slick.parent.domain.rx.PassThroughMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */
public class RouterPopularImpl implements RouterPopular {

    private final ApiTrakt apiTrakt;
    private final ApiTmdb apiTmdb;
    private final MapperMovie mapper;

    @Inject
    public RouterPopularImpl(ApiTrakt apiTrakt, ApiTmdb apiTmdb, MapperMovie mapper) {
        this.apiTrakt = apiTrakt;
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<MovieMetadata> popular() {
        return popular(1, 10);
    }

    @Override
    public Observable<MovieMetadata> popular(@IntRange(from = 1, to = Short.MAX_VALUE) int page,
                                             @IntRange(from = 1, to = Short.MAX_VALUE) int size) {
        if (page < 1) {
            throw new IllegalArgumentException("Page should be a positive number");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Size should be a positive number");
        }
        return apiTrakt.popular(page, size)
                .concatMap(Observable::fromIterable)
                .map((Function<MovieTraktMetadata, MovieMetadata>) mtm ->
                        MovieMetadataImpl.create(mtm.ids().tmdb(), mtm.ids().imdb(), mtm.title(), mtm.year()))
                .lift(new PassThroughMap<MovieMetadata>() {
                    @Override
                    public Observable<MovieMetadata> apply(@NonNull MovieMetadata movieMetadata) throws Exception {
                        return apiTmdb.movie(movieMetadata.id()).map(mapper).cast(MovieMetadata.class);
                    }
                });
    }

    @Override
    public Observable<MovieMetadata> page(
            @IntRange(from = 1, to = Short.MAX_VALUE) int page, @IntRange(from = 1, to = Short.MAX_VALUE) int size
    ) {
        return popular(page, size);
    }
}