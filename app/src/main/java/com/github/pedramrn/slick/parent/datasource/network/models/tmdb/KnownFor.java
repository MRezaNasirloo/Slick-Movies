package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */
@AutoValue
public abstract class KnownFor {
    @SerializedName("id")
    public abstract Integer id();

    @Nullable
    @SerializedName("title")
    public abstract String title();

    @Nullable
    @SerializedName("name")
    public abstract String name();

    @Nullable
    @SerializedName("poster_path")
    public abstract String posterPath();

    @Nullable
    @SerializedName("backdrop_path")
    public abstract String backdropPath();

    @Nullable
    @SerializedName("overview")
    public abstract String overview();

    @Nullable
    @SerializedName("media_type")
    public abstract String mediaType();

    @Nullable
    @SerializedName("vote_average")
    public abstract Integer voteAverage();

    @Nullable
    @SerializedName("vote_count")
    public abstract Integer voteCount();

    @Nullable
    @SerializedName("video")
    public abstract Boolean video();

    @Nullable
    @SerializedName("popularity")
    public abstract Float popularity();

    @Nullable
    @SerializedName("original_language")
    public abstract String originalLanguage();

    @Nullable
    @SerializedName("original_title")
    public abstract String originalTitle();

    @SerializedName("genre_ids")
    public abstract List<Integer> genreIds();

    @Nullable
    @SerializedName("adult")
    public abstract Boolean adult();

    @Nullable
    @SerializedName("release_date")
    public abstract String releaseDate();

    @Nullable
    @SerializedName("original_name")
    public abstract String originalName();

    @Nullable
    @SerializedName("first_air_date")
    public abstract String firstAirDate();

    @SerializedName("origin_country")
    public abstract List<String> originCountry();

    public static TypeAdapter<KnownFor> typeAdapter(Gson gson) {
        return new AutoValue_KnownFor.GsonTypeAdapter(gson);
    }
}
