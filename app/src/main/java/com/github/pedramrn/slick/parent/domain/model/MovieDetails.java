package com.github.pedramrn.slick.parent.domain.model;

import com.github.pedramrn.slick.parent.autovalue.IncludeHashEquals;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Cast;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Image;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */
@AutoValue
public abstract class MovieDetails {

    public abstract Boolean adult();

    public abstract String backdropPath();

    public abstract Object belongsToCollection();

    public abstract Integer budget();

    public abstract List<String> genres();

    public abstract String homepage();

    public abstract Integer id();

    public abstract String imdbId();

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

    public abstract Cast casts();

    public abstract Image images();

    @IncludeHashEquals
    public abstract String imdb();

    public abstract int trakt();


}
