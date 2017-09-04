package com.github.pedramrn.slick.parent.domain.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.CastOrCrewPersonDetailsTmdb;
import com.github.pedramrn.slick.parent.domain.model.CastOrCrewPersonDetailsDomain;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-03
 */
public class CastOrCrewPersonDetailsTmdbToDomain implements Function<CastOrCrewPersonDetailsTmdb, CastOrCrewPersonDetailsDomain> {
    @Override
    public CastOrCrewPersonDetailsDomain apply(@NonNull CastOrCrewPersonDetailsTmdb coc) throws Exception {
        return CastOrCrewPersonDetailsDomain.create(
                coc.id(),
                coc.creditId(),
                coc.character(),
                coc.job(),
                coc.department(),
                coc.episodeCount(),
                coc.name(),
                coc.originalName(),
                coc.title(),
                coc.originalTitle(),
                coc.overview(),
                coc.posterPath(),
                coc.backdropPath(),
                coc.firstAirDate(),
                coc.releaseDate(),
                coc.originalLanguage(),
                coc.adult(),
                coc.originCountry(),
                coc.genreIds(),
                coc.popularity(),
                coc.voteCount(),
                coc.voteAverage()
        );
    }
}
