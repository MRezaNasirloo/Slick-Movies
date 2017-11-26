package com.github.pedramrn.slick.parent.ui.people.mapper;

import com.github.pedramrn.slick.parent.domain.model.CastOrCrewPersonDetailsDomain;
import com.github.pedramrn.slick.parent.domain.model.PersonDetailsDomain;
import com.github.pedramrn.slick.parent.ui.people.model.CastOrCrewPersonDetails;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */
public class MapperPersonDetailsDomainPersonDetails implements Function<PersonDetailsDomain, PersonDetails> {

    private CastOrCrewPersonDetailsDomainToPresentation
            mapper = new CastOrCrewPersonDetailsDomainToPresentation();

    @Inject
    public MapperPersonDetailsDomainPersonDetails() {
    }


    @Override
    public PersonDetails apply(@NonNull PersonDetailsDomain pdd) throws Exception {
        return PersonDetails.create(
                pdd.id(),
                pdd.imdbId(),
                pdd.name(),
                pdd.biography(),
                pdd.placeOfBirth(),
                pdd.profilePicId(),
                pdd.gender(),
                pdd.birthday(),
                pdd.deathday(),
                pdd.alsoKnownAs(),
                pdd.popularity(),
                pdd.adult(),
                pdd.homepage(),
                pdd.images(),
                map(pdd.movieCast()),
                map(pdd.movieCrew()),
                map(pdd.tvCast()),
                map(pdd.tvCrew()
                ));
    }

    protected List<CastOrCrewPersonDetails> map(@NonNull List<CastOrCrewPersonDetailsDomain> pdd) {
        return Observable.fromIterable(pdd).map(mapper).sorted().toList().blockingGet();
    }

    private static class CastOrCrewPersonDetailsDomainToPresentation implements Function<CastOrCrewPersonDetailsDomain, CastOrCrewPersonDetails> {
        @Override
        public CastOrCrewPersonDetails apply(@NonNull CastOrCrewPersonDetailsDomain coc) throws Exception {
            return CastOrCrewPersonDetails.create(
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
}
