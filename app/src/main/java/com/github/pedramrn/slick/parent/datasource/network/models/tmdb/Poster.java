

package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.github.pedramrn.slick.parent.domain.SimpleData;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Poster implements SimpleData {


    @SerializedName("aspect_ratio")
    public abstract Float aspectRatio();
    @SerializedName("file_path")
    public abstract String filePath();
    @SerializedName("height")
    public abstract Integer height();
    @SerializedName("vote_average")
    public abstract Float voteAverage();
    @SerializedName("vote_count")
    public abstract Integer voteCount();
    @SerializedName("width")
    public abstract Integer width();

    @Override
    public String get() {
        return filePath();
    }

    public static Poster create(Float aspectRatio, String filePath, Integer height, Float voteAverage, Integer voteCount, Integer width) {
        return builder()
                .aspectRatio(aspectRatio)
                .filePath(filePath)
                .height(height)
                .voteAverage(voteAverage)
                .voteCount(voteCount)
                .width(width)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Poster.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder aspectRatio(Float aspectRatio);

        public abstract Builder filePath(String filePath);

        public abstract Builder height(Integer height);

        public abstract Builder voteCount(Integer voteCount);

        public abstract Builder width(Integer width);

        public abstract Builder voteAverage(Float voteAverage);

        public abstract Poster build();
    }

    public static TypeAdapter<Poster> typeAdapter(Gson gson) {
        return new AutoValue_Poster.GsonTypeAdapter(gson);
    }
}
