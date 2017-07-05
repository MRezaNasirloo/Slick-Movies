package com.github.pedramrn.slick.parent.datasource.network.models;

import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

@AutoValue
public abstract class BoxOfficeItem {
    @SerializedName("revenue")
    public abstract Long revenue();

    @SerializedName("movie")
    public abstract MovieTraktMetadata movie();

    public static TypeAdapter<BoxOfficeItem> typeAdapter(Gson gson) {
        return new AutoValue_BoxOfficeItem.GsonTypeAdapter(gson);
    }
}
