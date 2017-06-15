package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

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

    @SerializedName("cast_id")
    public abstract Integer castId();

    @SerializedName("credit_id")
    public abstract String creditId();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("profile_path")
    public abstract String profilePath();

    @SerializedName("character")
    public abstract String character();

    @SerializedName("gender")
    public abstract Integer gender();

    @SerializedName("order")
    public abstract Integer order();

    public static CastTmdb create(Integer id, Integer castId, String creditId, String name, String profilePath, String character, Integer gender,
                                  Integer order) {
        return new AutoValue_CastTmdb(id, castId, creditId, name, profilePath, character, gender, order);
    }


    public static TypeAdapter<CastTmdb> typeAdapter(Gson gson) {
        return new AutoValue_CastTmdb.GsonTypeAdapter(gson);
    }
}
