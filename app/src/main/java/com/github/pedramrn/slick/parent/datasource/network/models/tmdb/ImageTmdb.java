package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class ImageTmdb {

    @SerializedName("backdrops")
    public abstract List<Backdrop> backdrops();

    @SerializedName("posters")
    public abstract List<Poster> posters();

    public static TypeAdapter<ImageTmdb> typeAdapter(Gson gson) {
        return new AutoValue_ImageTmdb.GsonTypeAdapter(gson);
    }
}
