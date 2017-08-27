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
public abstract class Crew {

    @SerializedName("credit_id")
    public abstract String creditId();

    @SerializedName("department")
    public abstract String department();

    @SerializedName("gender")
    public abstract Integer gender();

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("job")
    public abstract String job();

    @SerializedName("name")
    public abstract String name();

    @Nullable
    @SerializedName("profile_path")
    public abstract String profilePath();

    public static TypeAdapter<Crew> typeAdapter(Gson gson) {
        return new AutoValue_Crew.GsonTypeAdapter(gson);
    }
}
