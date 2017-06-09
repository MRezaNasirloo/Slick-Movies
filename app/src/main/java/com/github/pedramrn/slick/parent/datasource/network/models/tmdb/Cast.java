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
public abstract class Cast {

    @SerializedName("cast_id")
    public abstract Integer castId();

    @SerializedName("character")
    public abstract String character();

    @SerializedName("credit_id")
    public abstract String creditId();

    @SerializedName("gender")
    public abstract Integer gender();

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("order")
    public abstract Integer order();

    @SerializedName("profile_path")
    public abstract Object profilePath();

    public static Cast create(Integer castId, String character, String creditId, Integer gender, Integer id, String name, Integer order,
                              Object profilePath) {
        return builder()
                .castId(castId)
                .character(character)
                .creditId(creditId)
                .gender(gender)
                .id(id)
                .name(name)
                .order(order)
                .profilePath(profilePath)
                .build();
    }

    public static Builder builder() {
        return new $AutoValue_Cast.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder castId(Integer castId);

        public abstract Builder character(String character);

        public abstract Builder creditId(String creditId);

        public abstract Builder gender(Integer gender);

        public abstract Builder id(Integer id);

        public abstract Builder name(String name);

        public abstract Builder order(Integer order);

        public abstract Builder profilePath(Object profilePath);

        public abstract Cast build();
    }

    public static TypeAdapter<Cast> typeAdapter(Gson gson) {
        return new AutoValue_Cast.GsonTypeAdapter(gson);
    }
}
