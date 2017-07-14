package com.github.pedramrn.slick.parent.ui.details.mapper;

import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */
public class MapperMovieSmallDomainMovieSmall implements Function<MovieSmallDomain, MovieSmall> {

    @Inject
    public MapperMovieSmallDomainMovieSmall() {
    }

    @Override
    public MovieSmall apply(@NonNull MovieSmallDomain msd) throws Exception {
        return MovieSmall.create(msd.id(),
                msd.title(),
                msd.overview(),
                msd.posterPath(),
                msd.backdropPath(),
                msd.releaseDate(),
                msd.voteAverageTmdb(),
                msd.voteCountTmdb(),
                null,
                null,
                null,
                msd.id(),
                msd.originalTitle(),
                msd.originalLanguage(),
                msd.adult(),
                msd.popularity(),
                msd.video()
        );
    }
}
