package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class CastTmdb {

    @SerializedName("id")
    public abstract Integer id();

    @Nullable
    @SerializedName("cast_id")
    public abstract Integer castId();

    @Nullable
    @SerializedName("credit_id")
    public abstract String creditId();

    @Nullable
    @SerializedName("name")
    public abstract String name();

    @Nullable
    @SerializedName("profile_path")
    public abstract String profilePath();

    @Nullable
    @SerializedName("character")
    public abstract String character();

    @Nullable
    @SerializedName("gender")
    public abstract Integer gender();

    @Nullable
    @SerializedName("order")
    public abstract Integer order();

    public static TypeAdapter<CastTmdb> typeAdapter(Gson gson) {
        return new AutoValue_CastTmdb.GsonTypeAdapter(gson);
    }
}
