package com.github.pedramrn.slick.parent.ui.people.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-03
 */

@AutoValue
public abstract class CastOrCrewPersonDetails implements Comparable<CastOrCrewPersonDetails> {

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

    public abstract Float popularity();

    public abstract Integer voteCount();

    public abstract Float voteAverage();

    @Nullable
    public String thumbnailPoster() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w342" + posterPath();
    }

    @Nullable
    public String thumbnailTinyPoster() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w92" + posterPath();
    }

    @Nullable
    public String thumbnailBackdrop() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + backdropPath();
    }

    @Override
    public int compareTo(@NonNull CastOrCrewPersonDetails o) {
        String releaseDate = releaseDate();
        if (releaseDate == null) return -1;
        String releaseDate1 = o.releaseDate();
        if (releaseDate1 == null) return 1;
        return releaseDate1.compareTo(releaseDate);
    }

    public static CastOrCrewPersonDetails create(Integer id, String creditId, String character, String job, String department, Integer episodeCount,
                                                 String name, String originalName, String title, String originalTitle, String overview,
                                                 String posterPath,
                                                 String backdropPath, String firstAirDate, String releaseDate, String originalLanguage, Boolean adult,
                                                 List<String> originCountry, List<Integer> genreIds, Float popularity, Integer voteCount,
                                                 Float voteAverage) {
        return new AutoValue_CastOrCrewPersonDetails(id, creditId, character, job, department, episodeCount, name, originalName, title, originalTitle,
                overview, posterPath, backdropPath, firstAirDate, releaseDate, originalLanguage, adult, originCountry, genreIds, popularity,
                voteCount, voteAverage);
    }

}
