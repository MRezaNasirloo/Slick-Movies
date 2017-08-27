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
public abstract class Ids {

    @SerializedName("trakt")
    public abstract Integer trakt();

    @SerializedName("slug")
    public abstract String slug();

    @SerializedName("imdb")
    public abstract String imdb();

    @SerializedName("tmdb")
    public abstract Integer tmdb();

    public static TypeAdapter<Ids> typeAdapter(Gson gson) {
        return new AutoValue_Ids.GsonTypeAdapter(gson);
    }


}
