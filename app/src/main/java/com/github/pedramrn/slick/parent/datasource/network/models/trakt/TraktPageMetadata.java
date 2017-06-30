package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */
@AutoValue
public abstract class TraktPageMetadata {

    // @SerializedName("list_count")
    // public abstract Integer listCount();

    @SerializedName("movie")
    public abstract MovieTraktMetadata movie();

    public static TraktPageMetadata create(MovieTraktMetadata movie) {
        return new AutoValue_TraktPageMetadata(movie);
    }

    /*public static TypeAdapter<TraktPageMetadata> typeAdapter(Gson gson) {
        return new AutoValue_TraktPageMetadata.GsonTypeAdapter(gson);
    }*/


}
