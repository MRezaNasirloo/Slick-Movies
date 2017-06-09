

package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Poster {


    @SerializedName("aspect_ratio")
    public abstract Float aspectRatio();
    @SerializedName("file_path")
    public abstract String filePath();
    @SerializedName("height")
    public abstract Integer height();
    @SerializedName("iso_639_1")
    public abstract String iso6391();
    @SerializedName("vote_average")
    public abstract Integer voteAverage();
    @SerializedName("vote_count")
    public abstract Integer voteCount();
    @SerializedName("width")
    public abstract Integer width();

    public static Poster create(Float aspectRatio, String filePath, Integer height, String iso6391, Integer voteAverage, Integer voteCount,
                                Integer width) {
        return builder()
                .aspectRatio(aspectRatio)
                .filePath(filePath)
                .height(height)
                .iso6391(iso6391)
                .voteAverage(voteAverage)
                .voteCount(voteCount)
                .width(width)
                .build();
    }

    public static Builder builder() {
        return new $AutoValue_Poster.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder aspectRatio(Float aspectRatio);

        public abstract Builder filePath(String filePath);

        public abstract Builder height(Integer height);

        public abstract Builder iso6391(String iso6391);

        public abstract Builder voteAverage(Integer voteAverage);

        public abstract Builder voteCount(Integer voteCount);

        public abstract Builder width(Integer width);

        public abstract Poster build();
    }

    public static TypeAdapter<Poster> typeAdapter(Gson gson) {
        return new AutoValue_Poster.GsonTypeAdapter(gson);
    }
}
