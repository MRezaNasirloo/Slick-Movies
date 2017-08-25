package com.github.pedramrn.slick.parent.ui.people.router;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.ImageFileTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonTmdb;
import com.github.pedramrn.slick.parent.domain.model.PersonDetailsDomain;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */
class MapperPersonTmdbPersonDetailsDomain implements Function<PersonTmdb, PersonDetailsDomain> {

    @Inject
    public MapperPersonTmdbPersonDetailsDomain() {
    }

    @Override
    public PersonDetailsDomain apply(@NonNull PersonTmdb pt) throws Exception {
        return PersonDetailsDomain.create(
                pt.id(),
                pt.imdbId(),
                pt.name(),
                pt.biography(),
                pt.placeOfBirth(),
                pt.profilePath(),
                pt.gender(),
                pt.birthday(),
                pt.deathday(),
                pt.alsoKnownAs(),
                pt.popularity(),
                pt.adult(),
                pt.homepage(),
                Observable.fromIterable(pt.images()).map(new Function<ImageFileTmdb, String>() {
                    @Override
                    public String apply(@NonNull ImageFileTmdb imageFileTmdb) throws Exception {
                        return imageFileTmdb.filePath();
                    }
                }).toList().blockingGet()

        );
    }
}
