package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */

@AutoValue
public abstract class MovieTmdbSmall {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("title")
    public abstract String title();

    @Nullable
    @SerializedName("poster_path")
    public abstract String posterPath();

    @Nullable
    @SerializedName("backdrop_path")
    public abstract String backdropPath();

    @SerializedName("adult")
    public abstract Boolean adult();

    @SerializedName("original_language")
    public abstract String originalLanguage();

    @SerializedName("original_title")
    public abstract String originalTitle();

    @Nullable
    @SerializedName("overview")
    public abstract String overview();

    @SerializedName("release_date")
    public abstract String releaseDate();

    @SerializedName("popularity")
    public abstract Float popularity();

    @SerializedName("video")
    public abstract Boolean video();

    @SerializedName("vote_average")
    public abstract Float voteAverage();

    @SerializedName("vote_count")
    public abstract Integer voteCount();

    // @SerializedName("genre_ids")
    // public abstract List<Integer> genreIds();


    public static TypeAdapter<MovieTmdbSmall> typeAdapter(Gson gson) {
        return new AutoValue_MovieTmdbSmall.GsonTypeAdapter(gson);
    }
}