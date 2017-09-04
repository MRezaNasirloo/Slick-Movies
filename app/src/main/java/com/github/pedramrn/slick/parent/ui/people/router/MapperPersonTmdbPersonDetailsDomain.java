package com.github.pedramrn.slick.parent.ui.people.router;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.CreditsPersonDetailsTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.ImageFileTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Profiles;
import com.github.pedramrn.slick.parent.domain.mapper.CastOrCrewPersonDetailsTmdbToDomain;
import com.github.pedramrn.slick.parent.domain.model.CastOrCrewPersonDetailsDomain;
import com.github.pedramrn.slick.parent.domain.model.PersonDetailsDomain;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */
class MapperPersonTmdbPersonDetailsDomain implements Function<PersonTmdb, PersonDetailsDomain> {

    private CastOrCrewPersonDetailsTmdbToDomain mapperCastOrCrew = new CastOrCrewPersonDetailsTmdbToDomain();

    @Inject
    public MapperPersonTmdbPersonDetailsDomain() {
    }

    @Override
    public PersonDetailsDomain apply(@NonNull PersonTmdb pt) throws Exception {
        CreditsPersonDetailsTmdb cpd = pt.creditsMovie();
        List<CastOrCrewPersonDetailsDomain> movieCast = Collections.emptyList();
        List<CastOrCrewPersonDetailsDomain> movieCrew = Collections.emptyList();
        List<CastOrCrewPersonDetailsDomain> tvCast = Collections.emptyList();
        List<CastOrCrewPersonDetailsDomain> tvCrew = Collections.emptyList();

        if (cpd != null) {
            movieCast = Observable.fromIterable(cpd.cast())
                    .map(mapperCastOrCrew)
                    .toList()
                    .blockingGet();
            movieCrew = Observable.fromIterable(cpd.crew())
                    .map(mapperCastOrCrew)
                    .toList()
                    .blockingGet();
        }

        cpd = pt.creditsTV();
        if (cpd != null) {
            tvCast = Observable.fromIterable(cpd.cast())
                    .map(mapperCastOrCrew)
                    .toList()
                    .blockingGet();
            tvCrew = Observable.fromIterable(cpd.crew())
                    .map(mapperCastOrCrew)
                    .toList()
                    .blockingGet();
        }

        List<String> images = Collections.emptyList();
        Profiles pti = pt.images();
        if (pti != null) {
            images = Observable.fromIterable(pti.profiles()).map(new Function<ImageFileTmdb, String>() {
                @Override
                public String apply(@NonNull ImageFileTmdb imageFileTmdb) throws Exception {
                    return imageFileTmdb.filePath();
                }
            }).toList().blockingGet();
        }
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
                images,
                movieCast,
                movieCrew,
                tvCast,
                tvCrew
        );
    }

}
