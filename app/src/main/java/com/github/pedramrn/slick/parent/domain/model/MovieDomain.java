package com.github.pedramrn.slick.parent.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */
@AutoValue
public abstract class MovieDomain {

    public abstract Integer id();

    public abstract String imdbId();

    public abstract Boolean adult();

    public abstract String backdropPath();

    public abstract Object belongsToCollection();

    public abstract Integer budget();

    public abstract List<String> genres();

    public abstract String homepage();

    public abstract String originalLanguage();

    public abstract String originalTitle();

    public abstract String overview();

    public abstract Float popularity();

    public abstract String posterPath();

    public abstract List<String> productionCompanies();

    public abstract List<String> productionCountries();

    public abstract String releaseDate();

    public abstract Integer revenue();

    public abstract Integer runtime();

    public abstract List<String> spokenLanguages();

    public abstract String status();

    public abstract String tagline();

    public abstract String title();

    public abstract Boolean video();

    public abstract Float voteAverage();

    public abstract Integer voteCount();

    public abstract List<CastDomain> casts();

    public abstract ImageDomain images();

    public abstract List<VideoDomain> videos();

    public static MovieDomain create(Integer id, String imdbId, Boolean adult, String backdropPath, Object belongsToCollection, Integer budget,
                                     List<String> genres, String homepage, String originalLanguage, String originalTitle, String overview,
                                     Float popularity, String posterPath, List<String> productionCompanies, List<String> productionCountries,
                                     String releaseDate, Integer revenue, Integer runtime, List<String> spokenLanguages, String status,
                                     String tagline,
                                     String title, Boolean video, Float voteAverage, Integer voteCount, List<CastDomain> casts, ImageDomain images,
                                     List<VideoDomain> videos) {
        return new AutoValue_MovieDomain(id, imdbId, adult, backdropPath, belongsToCollection, budget, genres, homepage, originalLanguage,
                originalTitle, overview, popularity, posterPath, productionCompanies, productionCountries, releaseDate, revenue, runtime,
                spokenLanguages, status, tagline, title, video, voteAverage, voteCount, casts, images, videos);
    }


}
