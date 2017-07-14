package com.github.pedramrn.slick.parent.domain.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdbSmall;
import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */
public class MapperMovieSmall implements Function<MovieTmdbSmall, MovieSmallDomain> {

    @Inject
    public MapperMovieSmall() {
    }

    @Override
    public MovieSmallDomain apply(@NonNull final MovieTmdbSmall mt) throws Exception {

        return MovieSmallDomain.create(mt.id(),
                mt.title(),
                mt.overview(),
                mt.originalTitle(),
                mt.posterPath(),
                mt.backdropPath(),
                mt.releaseDate(),
                mt.originalLanguage(),
                mt.adult(),
                mt.popularity(),
                mt.video(),
                mt.voteAverage(),
                mt.voteCount());
    }
}
