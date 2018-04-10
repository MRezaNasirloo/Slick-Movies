package com.github.pedramrn.slick.parent.ui.details.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdbSmall;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-09
 */
public class MovieTmdbSmallToMovieDomain implements Function<MovieTmdbSmall, MovieDomain> {

    @Inject
    public MovieTmdbSmallToMovieDomain() {
    }

    @Override
    public MovieDomain apply(MovieTmdbSmall mts) {
        return MovieDomain.builder()
                .id(mts.id())
                .title(mts.title())
                .posterPath(mts.posterPath())
                .backdropPath(mts.backdropPath())
                .adult(mts.adult())
                .originalLanguage(mts.originalLanguage())
                .originalTitle(mts.originalTitle())
                .overview(mts.overview())
                .releaseDate(mts.releaseDate())
                .popularity(mts.popularity())
                .video(mts.video())
                .voteCountTmdb(mts.voteCount())
                .voteAverageTmdb(mts.voteAverage())
                .genres(Collections.emptyList())
                .productionCompanies(Collections.emptyList())
                .productionCountries(Collections.emptyList())
                .spokenLanguages(Collections.emptyList())
                .casts(Collections.emptyList())
                .videos(Collections.emptyList())
                .build();
    }
}
