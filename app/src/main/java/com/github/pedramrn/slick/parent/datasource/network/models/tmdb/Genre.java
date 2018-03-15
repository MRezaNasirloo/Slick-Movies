package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.github.pedramrn.slick.parent.domain.SimpleData;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-02
 */
@AutoValue
public abstract class Genre implements SimpleData {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("name")
    public abstract String name();

    @Override
    public String get() {
        return name();
    }

    public static TypeAdapter<Genre> typeAdapter(Gson gson) {
        return new AutoValue_Genre.GsonTypeAdapter(gson);
    }
}
