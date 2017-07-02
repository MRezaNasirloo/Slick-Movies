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
public abstract class MovieTraktPageMetadata {

    // @SerializedName("list_count")
    // public abstract Integer listCount();

    @SerializedName("movie")
    public abstract MovieTraktMetadata movie();

    public static MovieTraktPageMetadata create(MovieTraktMetadata movie) {
        return new AutoValue_MovieTraktPageMetadata(movie);
    }

    public static TypeAdapter<MovieTraktPageMetadata> typeAdapter(Gson gson) {
        return new AutoValue_MovieTraktPageMetadata.GsonTypeAdapter(gson);
    }


}
