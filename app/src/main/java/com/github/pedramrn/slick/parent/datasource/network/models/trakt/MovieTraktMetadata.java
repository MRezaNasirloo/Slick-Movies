package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

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

    @SerializedName("title")
    public abstract String title();

    @SerializedName("year")
    public abstract Integer year();

    @SerializedName("ids")
    public abstract Ids ids();

    public static TypeAdapter<MovieTraktMetadata> typeAdapter(Gson gson) {
        return new AutoValue_MovieTraktMetadata.GsonTypeAdapter(gson);
    }
}
