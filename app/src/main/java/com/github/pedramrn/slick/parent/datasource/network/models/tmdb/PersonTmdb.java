package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */
@AutoValue
public abstract class PersonTmdb {
    @SerializedName("popularity")
    public abstract Float popularity();

    @SerializedName("id")
    public abstract Integer id();

    @Nullable
    @SerializedName("profile_path")
    public abstract String profilePath();

    @SerializedName("name")
    public abstract String name();

    // @SerializedName("known_for")
    // public abstract List<KnownFor> knownFor();

    @SerializedName("adult")
    public abstract Boolean adult();

    public static TypeAdapter<PersonTmdb> typeAdapter(Gson gson) {
        return new AutoValue_PersonTmdb.GsonTypeAdapter(gson);
    }
}
