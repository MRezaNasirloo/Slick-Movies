package com.github.pedramrn.slick.parent.ui.details.mapper;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-01
 */
public class MapperMovieDomainMovie implements Function<MovieDomain, Movie> {

    @Inject
    public MapperMovieDomainMovie() {
    }

    @Override
    public Movie apply(@NonNull MovieDomain md) throws Exception {
        return Movie.create(md);
    }
}
