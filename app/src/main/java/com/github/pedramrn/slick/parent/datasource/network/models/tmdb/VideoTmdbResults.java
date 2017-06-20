package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

@AutoValue
public abstract class VideoTmdbResults {
    @SerializedName("results")
    public abstract List<VideoTmdb> results();

    public static VideoTmdbResults create(List<VideoTmdb> results) {
        return new AutoValue_VideoTmdbResults(results);
    }

    public static TypeAdapter<VideoTmdbResults> typeAdapter(Gson gson) {
        return new AutoValue_VideoTmdbResults.GsonTypeAdapter(gson);
    }

}
