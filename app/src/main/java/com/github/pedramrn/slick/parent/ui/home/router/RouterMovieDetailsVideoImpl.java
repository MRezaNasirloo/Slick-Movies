package com.github.pedramrn.slick.parent.ui.home.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public class RouterMovieDetailsVideoImpl implements RouterMovieDetails {

    private final ApiTmdb apiTmdb;
    private final MapperMovie mapperMovie;

    @Inject
    public RouterMovieDetailsVideoImpl(ApiTmdb apiTmdb, MapperMovie mapperMovie) {
        this.apiTmdb = apiTmdb;
        this.mapperMovie = mapperMovie;
    }

    @Override
    public Observable<MovieDetails> get(final Integer tmdbId) {
        return apiTmdb.withVideos(tmdbId).map(mapperMovie);
    }

}
