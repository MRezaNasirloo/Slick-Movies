package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-07-14
 */

@AutoValue
public abstract class MovieFind {

    @SerializedName("movie_results")
    public abstract List<MovieTmdbSmall> movies();

    public static TypeAdapter<MovieFind> typeAdapter(Gson gson) {
        return new AutoValue_MovieFind.GsonTypeAdapter(gson);
    }

}
