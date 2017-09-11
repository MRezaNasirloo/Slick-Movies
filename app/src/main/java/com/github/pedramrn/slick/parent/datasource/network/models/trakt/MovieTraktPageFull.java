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
public abstract class MovieTraktPageFull {

    // @SerializedName("list_count")
    // public abstract Integer listCount();

    @SerializedName("movie")
    public abstract MovieTraktPageFull movie();

    public static TypeAdapter<MovieTraktPageFull> typeAdapter(Gson gson) {
        return new AutoValue_MovieTraktPageFull.GsonTypeAdapter(gson);
    }


}
