package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-27
 */

@AutoValue
public abstract class CastOrCrewPersonDetailsDomain implements Comparable<CastOrCrewPersonDetailsDomain> {

    //crew/cast data

    public abstract Integer id();

    public abstract String creditId();

    /**
     * NOTE: for casts only, otherwise null will be returned
     */
    @Nullable
    public abstract String character();

    /**
     * NOTE: for crews only, otherwise null will be returned
     */
    @Nullable
    public abstract String job();

    /**
     * NOTE: for crews only, otherwise null will be returned
     */
    @Nullable
    public abstract String department();

    /**
     * NOTE: for TV shows only, otherwise returns null
     *
     * @return the episode counts that this crew/cast has took part in it.
     */
    @Nullable
    public abstract Integer episodeCount();

    //movie or tv data

    /**
     * NOTE: for TV shows only, otherwise returns null
     */
    @Nullable
    public abstract String name();

    /**
     * NOTE: for TV shows only, otherwise returns null
     */
    @Nullable
    public abstract String originalName();

    /**
     * NOTE: for Movies only, otherwise returns null
     */
    @Nullable
    public abstract String title();

    /**
     * NOTE: for Movies only, otherwise returns null
     */
    @Nullable
    public abstract String originalTitle();

    @Nullable
    public abstract String overview();

    @Nullable
    public abstract String posterPath();

    @Nullable
    public abstract String backdropPath();

    /**
     * NOTE: for TV shows only, otherwise returns null
     */
    @Nullable
    public abstract String firstAirDate();

    /**
     * NOTE: for Movies only, otherwise returns null
     */
    @Nullable
    public abstract String releaseDate();

    @Nullable
    public abstract String originalLanguage();

    @Nullable
    public abstract Boolean adult();

    public abstract List<String> originCountry();

    public abstract List<Integer> genreIds();

    @Nullable
    public abstract Float popularity();

    @Nullable
    public abstract Integer voteCount();

    @Nullable
    public abstract Float voteAverage();

    @Override
    public int compareTo(@NonNull CastOrCrewPersonDetailsDomain o) {
        String releaseDate = releaseDate();
        if (releaseDate == null) return -1;
        String releaseDate1 = o.releaseDate();
        if (releaseDate1 == null) return 1;
        return releaseDate1.compareTo(releaseDate);
    }

    public static CastOrCrewPersonDetailsDomain create(Integer id, String creditId, String character, String job, String department,
                                                       Integer episodeCount,
                                                       String name, String originalName, String title, String originalTitle, String overview,
                                                       String posterPath, String backdropPath, String firstAirDate, String releaseDate,
                                                       String originalLanguage, Boolean adult, List<String> originCountry, List<Integer> genreIds,
                                                       Float popularity, Integer voteCount, Float voteAverage) {
        return new AutoValue_CastOrCrewPersonDetailsDomain(id, creditId, character, job, department, episodeCount, name, originalName, title,
                originalTitle, overview, posterPath, backdropPath, firstAirDate, releaseDate, originalLanguage, adult, originCountry, genreIds,
                popularity, voteCount, voteAverage);
    }

}
