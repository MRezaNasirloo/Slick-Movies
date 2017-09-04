package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-19
 */

@AutoValue
public abstract class PersonTmdb {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("imdb_id")
    public abstract String imdbId();

    @SerializedName("name")
    public abstract String name();

    @Nullable
    @SerializedName("biography")
    public abstract String biography();

    @Nullable
    @SerializedName("place_of_birth")
    public abstract String placeOfBirth();

    @Nullable
    @SerializedName("profile_path")
    public abstract String profilePath();

    @Nullable
    @SerializedName("gender")
    public abstract Integer gender();

    @Nullable
    @SerializedName("birthday")
    public abstract String birthday();

    @Nullable
    @SerializedName("deathday")
    public abstract String deathday();

    @SerializedName("also_known_as")
    public abstract List<String> alsoKnownAs();

    @SerializedName("popularity")
    public abstract Float popularity();

    @SerializedName("adult")
    public abstract Boolean adult();

    @Nullable
    @SerializedName("homepage")
    public abstract String homepage();

    @Nullable
    @SerializedName("images")
    public abstract Profiles images();

    @Nullable
    @SerializedName("movie_credits")
    public abstract CreditsPersonDetailsTmdb creditsMovie();

    @Nullable
    @SerializedName("tv_credits")
    public abstract CreditsPersonDetailsTmdb creditsTV();


    public static TypeAdapter<PersonTmdb> typeAdapter(Gson gson) {
        return new AutoValue_PersonTmdb.GsonTypeAdapter(gson);
    }
}


