package com.github.pedramrn.slick.parent.ui.people.mapper;

import com.github.pedramrn.slick.parent.domain.model.PersonDetailsDomain;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */
public class MapperPersonDetailsDomainPersonDetails implements Function<PersonDetailsDomain, PersonDetails> {

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
                pdd.images()
        );
    }
}
