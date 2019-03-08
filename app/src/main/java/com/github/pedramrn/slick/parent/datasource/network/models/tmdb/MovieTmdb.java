package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */

@AutoValue
public abstract class MovieTmdb {

    @SerializedName("id")
    public abstract Integer id();

    @Nullable
    @SerializedName("imdb_id")
    public abstract String imdbId();

    @Nullable
    @SerializedName("adult")
    public abstract Boolean adult();

    @Nullable
    @SerializedName("backdrop_path")
    public abstract String backdropPath();

    @Nullable
    @SerializedName("belongs_to_collection")
    public abstract Object belongsToCollection();

    @Nullable
    @SerializedName("budget")
    public abstract Integer budget();

    @SerializedName("genres")
    public abstract List<Genre> genres();

    @Nullable
    @SerializedName("homepage")
    public abstract String homepage();

    @Nullable
    @SerializedName("original_language")
    public abstract String originalLanguage();

    @Nullable
    @SerializedName("original_title")
    public abstract String originalTitle();

    @Nullable
    @SerializedName("overview")
    public abstract String overview();

    @Nullable
    @SerializedName("popularity")
    public abstract Float popularity();

    @Nullable
    @SerializedName("poster_path")
    public abstract String posterPath();

    @SerializedName("production_companies")
    public abstract List<ProductionCompany> productionCompanies();

    @SerializedName("production_countries")
    public abstract List<ProductionCountry> productionCountries();

    @Nullable
    @SerializedName("release_date")
    public abstract String releaseDate();

    @Nullable
    @SerializedName("revenue")
    public abstract Long revenue();

    @Nullable
    @SerializedName("runtime")
    public abstract Integer runtime();

    @SerializedName("spoken_languages")
    public abstract List<SpokenLanguage> spokenLanguages();

    @Nullable
    @SerializedName("status")
    public abstract String status();

    @Nullable
    @SerializedName("tagline")
    public abstract String tagline();

    @Nullable
    @SerializedName("title")
    public abstract String title();

    @Nullable
    @SerializedName("video")
    public abstract Boolean video();

    @Nullable
    @SerializedName("vote_average")
    public abstract Float voteAverage();

    @Nullable
    @SerializedName("vote_count")
    public abstract Integer voteCount();

    @Nullable
    @SerializedName("credits")
    public abstract Credit credits();

    @Nullable
    @SerializedName("images")
    public abstract ImageTmdb images();

    @Nullable
    @SerializedName("videos")
    public abstract VideoTmdbResults videos();

    @Nullable
    @SerializedName("release_dates")
    public abstract JsonObject releaseDates();

    public abstract Builder toBuilder();

    public static TypeAdapter<MovieTmdb> typeAdapter(Gson gson) {
        return new AutoValue_MovieTmdb.GsonTypeAdapter(gson);
    }

    public static Builder builder() {return new AutoValue_MovieTmdb.Builder();}

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder imdbId(String imdbId);

        public abstract Builder adult(Boolean adult);

        public abstract Builder backdropPath(String backdropPath);

        public abstract Builder belongsToCollection(Object belongsToCollection);

        public abstract Builder budget(Integer budget);

        public abstract Builder genres(List<Genre> genres);

        public abstract Builder homepage(String homepage);

        public abstract Builder originalLanguage(String originalLanguage);

        public abstract Builder originalTitle(String originalTitle);

        public abstract Builder overview(String overview);

        public abstract Builder popularity(Float popularity);

        public abstract Builder posterPath(String posterPath);

        public abstract Builder productionCompanies(List<ProductionCompany> productionCompanies);

        public abstract Builder productionCountries(List<ProductionCountry> productionCountries);

        public abstract Builder releaseDate(String releaseDate);

        public abstract Builder revenue(Long revenue);

        public abstract Builder runtime(Integer runtime);

        public abstract Builder spokenLanguages(List<SpokenLanguage> spokenLanguages);

        public abstract Builder status(String status);

        public abstract Builder tagline(String tagline);

        public abstract Builder title(String title);

        public abstract Builder video(Boolean video);

        public abstract Builder voteAverage(Float voteAverage);

        public abstract Builder voteCount(Integer voteCount);

        public abstract Builder credits(Credit credits);

        public abstract Builder images(ImageTmdb images);

        public abstract Builder videos(VideoTmdbResults videos);

        public abstract Builder releaseDates(JsonObject jsonObject);

        public abstract MovieTmdb build();
    }
}