package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-19
 */
@AutoValue
public abstract class ImageFileTmdb {

    @SerializedName("file_path")
    public abstract String filePath();

    @Nullable
    @SerializedName("width")
    public abstract Integer width();

    @Nullable
    @SerializedName("height")
    public abstract Integer height();

    @Nullable
    @SerializedName("aspect_ratio")
    public abstract Float aspectRatio();

    @Nullable
    @SerializedName("vote_average")
    public abstract Float voteAverage();

    @Nullable
    @SerializedName("vote_count")
    public abstract Integer voteCount();

    public static TypeAdapter<ImageFileTmdb> typeAdapter(Gson gson) {
        return new AutoValue_ImageFileTmdb.GsonTypeAdapter(gson);
    }
}
