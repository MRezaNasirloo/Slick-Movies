package com.github.pedramrn.slick.parent.ui.favorite.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovie;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public class RouterMovieImpl implements RouterMovie {

    private final ApiTmdb apiTmdb;
    private final MapperMovie mapper;

    @Inject
    public RouterMovieImpl(ApiTmdb apiTmdb, MapperMovie mapper) {
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<MovieDomain> movie(Integer tmdbId) {
        return apiTmdb.movie(tmdbId).map(mapper);
    }

    @Override
    public Observable<MovieDomain> movie(Integer... tmdbIds) {
        return Observable.fromArray(tmdbIds)
                .flatMap(this::movie);
    }

    @Override
    public Observable<MovieDomain> movie(List<FavoriteDomain> favorites) {
        return Observable.fromIterable(favorites)
                .flatMap(favorite -> this.movie(favorite.tmdb()));
    }
}
