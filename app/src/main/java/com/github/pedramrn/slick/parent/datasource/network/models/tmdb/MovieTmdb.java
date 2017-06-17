package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

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

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("imdb_id")
    public abstract String imdbId();

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

    @Nullable
    @SerializedName("credits")
    public abstract Credit credits();

    @Nullable
    @SerializedName("images")
    public abstract ImageTmdb images();

    public static MovieTmdb create(Integer id, String imdbId, Boolean adult, String backdropPath, Object belongsToCollection, Integer budget,
                                   List<Genre> genres, String homepage, String originalLanguage, String originalTitle, String overview,
                                   Float popularity,
                                   String posterPath, List<ProductionCompany> productionCompanies, List<ProductionCountry> productionCountries,
                                   String releaseDate, Integer revenue, Integer runtime, List<SpokenLanguage> spokenLanguages, String status,
                                   String tagline, String title, Boolean video, Float voteAverage, Integer voteCount, Credit credits, ImageTmdb images) {
        return new AutoValue_MovieTmdb(id, imdbId, adult, backdropPath, belongsToCollection, budget, genres, homepage, originalLanguage,
                originalTitle, overview, popularity, posterPath, productionCompanies, productionCountries, releaseDate, revenue, runtime,
                spokenLanguages, status, tagline, title, video, voteAverage, voteCount, credits, images);
    }


    public static TypeAdapter<MovieTmdb> typeAdapter(Gson gson) {
        return new AutoValue_MovieTmdb.GsonTypeAdapter(gson);
    }

}