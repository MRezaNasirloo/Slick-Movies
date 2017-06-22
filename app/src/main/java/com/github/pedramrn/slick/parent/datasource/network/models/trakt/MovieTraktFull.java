package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */
@AutoValue
public abstract class MovieTraktFull {

    @SerializedName("title")
    public abstract String title();

    @SerializedName("year")
    public abstract Integer year();

    @SerializedName("ids")
    public abstract Ids ids();

    @SerializedName("tagline")
    public abstract String tagline();

    @SerializedName("overview")
    public abstract String overview();

    @SerializedName("released")
    public abstract String released();

    @SerializedName("runtime")
    public abstract Integer runtime();

    @Nullable
    @SerializedName("trailer")
    public abstract String trailer();

    @Nullable
    @SerializedName("homepage")
    public abstract String homepage();

    @SerializedName("rating")
    public abstract Float rating();

    @SerializedName("votes")
    public abstract Integer votes();

    @SerializedName("updated_at")
    public abstract String updatedAt();

    @SerializedName("language")
    public abstract String language();

    @SerializedName("available_translations")
    public abstract List<String> availableTranslations();

    @SerializedName("genres")
    public abstract List<String> genres();

    @Nullable
    @SerializedName("certification")
    public abstract String certification();

    public static MovieTraktFull create(String title, Integer year, Ids ids, String tagline, String overview, String released, Integer runtime,
                                        String trailer, String homepage, Float rating, Integer votes, String updatedAt, String language,
                                        List<String> availableTranslations, List<String> genres, String certification) {
        return new AutoValue_MovieTraktFull(title, year, ids, tagline, overview, released, runtime, trailer, homepage, rating, votes, updatedAt,
                language, availableTranslations, genres, certification);
    }

    public static TypeAdapter<MovieTraktFull> typeAdapter(Gson gson) {
        return new AutoValue_MovieTraktFull.GsonTypeAdapter(gson);
    }

}
