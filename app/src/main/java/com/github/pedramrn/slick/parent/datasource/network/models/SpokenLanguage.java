package com.github.pedramrn.slick.parent.datasource.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-02
 */
@AutoValue
public abstract class SpokenLanguage {

    @SerializedName("iso_639_1")
    public abstract String iso6391();

    @SerializedName("name")
    public abstract String name();

    public static SpokenLanguage create(String iso6391, String name) {
        return builder()
                .iso6391(iso6391)
                .name(name)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_SpokenLanguage.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder iso6391(String iso6391);

        public abstract Builder name(String name);

        public abstract SpokenLanguage build();
    }

    public static TypeAdapter<SpokenLanguage> typeAdapter(Gson gson) {
        return new AutoValue_SpokenLanguage.GsonTypeAdapter(gson);
    }

}
