package com.github.pedramrn.slick.parent.datasource.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */

@AutoValue
public abstract class MovieTmdb {

    @SerializedName("adult")
    public abstract Boolean adult();

    @SerializedName("backdrop_path")
    public abstract String backdropPath();

    @SerializedName("belongs_to_collection")
    public abstract Object belongsToCollection();

    @SerializedName("budget")
    public abstract Integer budget();

    @SerializedName("genres")
    public abstract List<Genre> genres();

    @SerializedName("homepage")
    public abstract String homepage();

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("imdb_id")
    public abstract String imdbId();

    @SerializedName("original_language")
    public abstract String originalLanguage();

    @SerializedName("original_title")
    public abstract String originalTitle();

    @SerializedName("overview")
    public abstract String overview();

    @SerializedName("popularity")
    public abstract Float popularity();

    @SerializedName("poster_path")
    public abstract String posterPath();

    @SerializedName("production_companies")
    public abstract List<ProductionCompany> productionCompanies();

    @SerializedName("production_countries")
    public abstract List<ProductionCountry> productionCountries();

    @SerializedName("release_date")
    public abstract String releaseDate();

    @SerializedName("revenue")
    public abstract Integer revenue();

    @SerializedName("runtime")
    public abstract Integer runtime();

    @SerializedName("spoken_languages")
    public abstract List<SpokenLanguage> spokenLanguages();

    @SerializedName("status")
    public abstract String status();

    @SerializedName("tagline")
    public abstract String tagline();

    @SerializedName("title")
    public abstract String title();

    @SerializedName("video")
    public abstract Boolean video();

    @SerializedName("vote_average")
    public abstract Float voteAverage();

    @SerializedName("vote_count")
    public abstract Integer voteCount();

    public static MovieTmdb create(Boolean adult, String backdropPath, Object belongsToCollection, Integer budget, List<Genre> genres,
                                   String homepage,
                                   Integer id, String imdbId, String originalLanguage, String originalTitle, String overview, Float popularity,
                                   String posterPath, List<ProductionCompany> productionCompanies, List<ProductionCountry> productionCountries,
                                   String releaseDate, Integer revenue, Integer runtime, List<SpokenLanguage> spokenLanguages, String status,
                                   String tagline, String title, Boolean video, Float voteAverage, Integer voteCount) {
        return builder()
                .adult(adult)
                .backdropPath(backdropPath)
                .belongsToCollection(belongsToCollection)
                .budget(budget)
                .genres(genres)
                .homepage(homepage)
                .id(id)
                .imdbId(imdbId)
                .originalLanguage(originalLanguage)
                .originalTitle(originalTitle)
                .overview(overview)
                .popularity(popularity)
                .posterPath(posterPath)
                .productionCompanies(productionCompanies)
                .productionCountries(productionCountries)
                .releaseDate(releaseDate)
                .revenue(revenue)
                .runtime(runtime)
                .spokenLanguages(spokenLanguages)
                .status(status)
                .tagline(tagline)
                .title(title)
                .video(video)
                .voteAverage(voteAverage)
                .voteCount(voteCount)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_MovieTmdb.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder adult(Boolean adult);

        public abstract Builder backdropPath(String backdropPath);

        public abstract Builder belongsToCollection(Object belongsToCollection);

        public abstract Builder budget(Integer budget);

        public abstract Builder genres(List<Genre> genres);

        public abstract Builder homepage(String homepage);

        public abstract Builder id(Integer id);

        public abstract Builder imdbId(String imdbId);

        public abstract Builder originalLanguage(String originalLanguage);

        public abstract Builder originalTitle(String originalTitle);

        public abstract Builder overview(String overview);

        public abstract Builder popularity(Float popularity);

        public abstract Builder posterPath(String posterPath);

        public abstract Builder productionCompanies(List<ProductionCompany> productionCompanies);

        public abstract Builder productionCountries(List<ProductionCountry> productionCountries);

        public abstract Builder releaseDate(String releaseDate);

        public abstract Builder revenue(Integer revenue);

        public abstract Builder runtime(Integer runtime);

        public abstract Builder spokenLanguages(List<SpokenLanguage> spokenLanguages);

        public abstract Builder status(String status);

        public abstract Builder tagline(String tagline);

        public abstract Builder title(String title);

        public abstract Builder video(Boolean video);

        public abstract Builder voteAverage(Float voteAverage);

        public abstract Builder voteCount(Integer voteCount);

        public abstract MovieTmdb build();
    }

    public static TypeAdapter<MovieTmdb> typeAdapter(Gson gson) {
        return new AutoValue_MovieTmdb.GsonTypeAdapter(gson);
    }
}