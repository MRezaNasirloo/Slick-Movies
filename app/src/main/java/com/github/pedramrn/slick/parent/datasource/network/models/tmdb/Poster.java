

package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

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

    @Nullable
    @SerializedName("aspect_ratio")
    public abstract Float aspectRatio();

    @SerializedName("file_path")
    public abstract String filePath();

    @Nullable
    @SerializedName("height")
    public abstract Integer height();

    @Nullable
    @SerializedName("vote_average")
    public abstract Float voteAverage();

    @Nullable
    @SerializedName("vote_count")
    public abstract Integer voteCount();

    @Nullable
    @SerializedName("width")
    public abstract Integer width();

    @Override
    public String get() {
        return filePath();
    }

    public static TypeAdapter<Poster> typeAdapter(Gson gson) {
        return new AutoValue_Poster.GsonTypeAdapter(gson);
    }
}
