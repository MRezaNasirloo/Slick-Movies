package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

@AutoValue
public abstract class PersonDetailsDomain {
    public abstract Integer id();

    @Nullable
    public abstract String imdbId();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String biography();

    @Nullable
    public abstract String placeOfBirth();

    @Nullable
    public abstract String profilePicId();

    @Nullable
    public abstract Integer gender();

    @Nullable
    public abstract String birthday();

    @Nullable
    public abstract String deathday();

    public abstract List<String> alsoKnownAs();

    public abstract Float popularity();

    public abstract Boolean adult();

    @Nullable
    public abstract String homepage();

    public abstract List<String> images();

    public abstract List<CastOrCrewPersonDetailsDomain> movieCast();

    public abstract List<CastOrCrewPersonDetailsDomain> movieCrew();

    public abstract List<CastOrCrewPersonDetailsDomain> tvCast();

    public abstract List<CastOrCrewPersonDetailsDomain> tvCrew();

    public static PersonDetailsDomain create(Integer id, String imdbId, String name, String biography, String placeOfBirth, String profilePicId,
                                             Integer gender, String birthday, String deathday, List<String> alsoKnownAs, Float popularity,
                                             Boolean adult,
                                             String homepage, List<String> images, List<CastOrCrewPersonDetailsDomain> movieCast,
                                             List<CastOrCrewPersonDetailsDomain> movieCrew, List<CastOrCrewPersonDetailsDomain> tvCast,
                                             List<CastOrCrewPersonDetailsDomain> tvCrew) {
        return new AutoValue_PersonDetailsDomain(id, imdbId, name, biography, placeOfBirth, profilePicId, gender, birthday, deathday, alsoKnownAs,
                popularity, adult, homepage, images, movieCast, movieCrew, tvCast, tvCrew);
    }

}
