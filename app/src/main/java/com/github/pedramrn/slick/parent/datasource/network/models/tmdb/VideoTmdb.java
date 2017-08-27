package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

@AutoValue
public abstract class VideoTmdb {
    @SerializedName("type")
    public abstract String type();

    @SerializedName("key")
    public abstract String key();

    @SerializedName("name")
    public abstract String name();

    public static TypeAdapter<VideoTmdb> typeAdapter(Gson gson) {
        return new AutoValue_VideoTmdb.GsonTypeAdapter(gson);
    }

}
