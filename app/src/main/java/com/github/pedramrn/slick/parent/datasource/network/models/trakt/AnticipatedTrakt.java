package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */
@AutoValue
public abstract class AnticipatedTrakt {

    @SerializedName("list_count")
    public abstract Integer listCount();

    @SerializedName("movie")
    public abstract List<MovieTraktFull> movie();

    public static AnticipatedTrakt create(Integer listCount, List<MovieTraktFull> movie) {
        return new AutoValue_AnticipatedTrakt(listCount, movie);
    }

    public static TypeAdapter<AnticipatedTrakt> typeAdapter(Gson gson) {
        return new AutoValue_AnticipatedTrakt.GsonTypeAdapter(gson);
    }


}
