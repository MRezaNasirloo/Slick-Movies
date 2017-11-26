package com.github.pedramrn.slick.parent.ui.details.mapper;

import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.github.pedramrn.slick.parent.util.DateUtils.year;

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
        return MovieSmall.create(
                msd.id(),
                null,
                msd.title(),
                year(msd.releaseDate()),
                msd.overview(),
                msd.posterPath(),
                msd.backdropPath(),
                msd.releaseDate(),
                msd.voteAverageTmdb(),
                msd.voteCountTmdb(),
                null,
                null,
                null,
                null,
                msd.originalTitle(),
                msd.originalLanguage(),
                msd.adult(),
                msd.popularity(),
                msd.video()
        );
    }
}
