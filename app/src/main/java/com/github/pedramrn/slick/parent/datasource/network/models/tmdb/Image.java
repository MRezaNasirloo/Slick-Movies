package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Image {


    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("backdrops")
    public abstract List<Backdrop> backdrops();

    @SerializedName("posters")
    public abstract List<Poster> posters();

    public static Image create(Integer id, List<Backdrop> backdrops, List<Poster> posters) {
        return builder()
                .id(id)
                .backdrops(backdrops)
                .posters(posters)
                .build();
    }

    public static Builder builder() {
        return new $AutoValue_Image.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder backdrops(List<Backdrop> backdrops);

        public abstract Builder posters(List<Poster> posters);

        public abstract Image build();
    }

    public static TypeAdapter<Image> typeAdapter(Gson gson) {
        return new AutoValue_Image.GsonTypeAdapter(gson);
    }
}
