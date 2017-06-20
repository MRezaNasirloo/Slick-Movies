package com.github.pedramrn.slick.parent.ui.details.model;

import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */

@AutoValue
public abstract class Movie {

    public abstract Integer id();

    public abstract String imdbId();

    public abstract Boolean adult();

    public abstract String backdropPath();

    public abstract Integer budget();

    public abstract List<String> genres();

    public abstract String homepage();

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

    public abstract List<Cast> casts();

    public abstract Image images();

    public abstract List<Video> videos();

    public static Movie create(Integer id, String imdbId, Boolean adult, String backdropPath, Integer budget, List<String> genres, String homepage,
                               String overview, Float popularity, String posterPath, List<String> productionCompanies,
                               List<String> productionCountries,
                               String releaseDate, Integer revenue, Integer runtime, List<String> spokenLanguages, String status, String tagline,
                               String title, Boolean video, Float voteAverage, Integer voteCount, List<Cast> casts, Image images,
                               List<Video> videos) {
        return new AutoValue_Movie(id, imdbId, adult, backdropPath, budget, genres, homepage, overview, popularity, posterPath, productionCompanies,
                productionCountries, releaseDate, revenue, runtime, spokenLanguages, status, tagline, title, video, voteAverage, voteCount, casts,
                images, videos);
    }

}
