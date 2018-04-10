package com.github.pedramrn.slick.parent.ui.details.router;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieFind;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.rx.PassThroughMap;
import com.github.pedramrn.slick.parent.ui.details.mapper.MovieTmdbSmallToMovieDomain;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-15
 */

public class RouterMovieDetailsIMDBImpl implements RouterMovieDetails {

    private final ApiTmdb apiTmdb;
    private MovieTmdbSmallToMovieDomain mapper;
    private final MapperMovie mapperMovie;
    private PassThroughMap<MovieDomain> lifter;

    @Inject
    public RouterMovieDetailsIMDBImpl(ApiTmdb apiTmdb, MovieTmdbSmallToMovieDomain mapper, MapperMovie mapperMovie,
            MovieDomainPassThroughMap lifter) {
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
        this.mapperMovie = mapperMovie;
        this.lifter = lifter;
    }

    @Override
    public Observable<MovieDomain> get(@NonNull final String movieId) {
        return apiTmdb.findMovie(movieId)
                .map(MovieFind::movies)
                .flatMap(Observable::fromIterable)
                .map(mapper)
                .map(movieDomain -> movieDomain.toBuilder().imdbId(movieId).build())
                .lift(new PassThroughMap<MovieDomain>() {
                    @Override
                    public Observable<MovieDomain> apply(MovieDomain movieDomain) {
                        return apiTmdb.movieFull(movieDomain.id()).map(mapperMovie);
                    }
                })
                .lift(lifter);
    }

    @Override
    public Observable<MovieDomain> get(Integer movieId) {
        return get(movieId.toString());
    }

}
