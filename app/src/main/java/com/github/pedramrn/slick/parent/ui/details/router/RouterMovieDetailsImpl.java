package com.github.pedramrn.slick.parent.ui.details.router;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.rx.PassThroughMap;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-15
 */

public class RouterMovieDetailsImpl implements RouterMovieDetails {

    private final ApiTmdb apiTmdb;
    private final MapperMovie mapperMovie;
    private final PassThroughMap<MovieDomain> lifter;

    @Inject
    public RouterMovieDetailsImpl(ApiTmdb apiTmdb, MapperMovie mapperMovie, MovieDomainPassThroughMap lifter) {
        this.apiTmdb = apiTmdb;
        this.lifter = lifter;
        this.mapperMovie = mapperMovie;
    }

    @Override
    public Observable<MovieDomain> get(@NonNull final String movieId) {
        return get(Integer.valueOf(movieId));
    }

    @Override
    public Observable<MovieDomain> get(Integer movieId) {
        return apiTmdb.movieFull(movieId)
                .map(mapperMovie)
                .lift(lifter);
    }
}
