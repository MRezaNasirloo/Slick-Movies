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

    @Nullable
    @SerializedName("title")
    public abstract String title();

    @Nullable
    @SerializedName("poster_path")
    public abstract String posterPath();

    @Nullable
    @SerializedName("backdrop_path")
    public abstract String backdropPath();

    @Nullable
    @SerializedName("adult")
    public abstract Boolean adult();

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
    @SerializedName("release_date")
    public abstract String releaseDate();

    @Nullable
    @SerializedName("popularity")
    public abstract Float popularity();

    @Nullable
    @SerializedName("video")
    public abstract Boolean video();

    @Nullable
    @SerializedName("vote_average")
    public abstract Float voteAverage();

    @Nullable
    @SerializedName("vote_count")
    public abstract Integer voteCount();

    // @SerializedName("genre_ids")
    // public abstract List<Integer> genreIds();


    public static TypeAdapter<MovieTmdbSmall> typeAdapter(Gson gson) {
        return new AutoValue_MovieTmdbSmall.GsonTypeAdapter(gson);
    }
}