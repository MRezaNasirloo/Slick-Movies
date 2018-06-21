package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

@AutoValue
public abstract class MovieTraktMetadata {

    @Nullable
    @SerializedName("title")
    public abstract String title();

    @Nullable
    @SerializedName("year")
    public abstract Integer year();

    @SerializedName("ids")
    public abstract Ids ids();

    public static TypeAdapter<MovieTraktMetadata> typeAdapter(Gson gson) {
        return new AutoValue_MovieTraktMetadata.GsonTypeAdapter(gson);
    }
}
