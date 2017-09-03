package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-27
 */

@AutoValue
public abstract class Profiles {
    @SerializedName("profiles")
    public abstract List<ImageFileTmdb> profiles();

    public static TypeAdapter<Profiles> typeAdapter(Gson gson) {
        return new AutoValue_Profiles.GsonTypeAdapter(gson);
    }
}
