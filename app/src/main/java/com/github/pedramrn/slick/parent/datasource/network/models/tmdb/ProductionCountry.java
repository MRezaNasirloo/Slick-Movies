package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-02
 */
@AutoValue
public abstract class ProductionCountry {

    @SerializedName("iso_3166_1")
    public abstract String iso31661();
    @SerializedName("name")
    public abstract String name();

    public static ProductionCountry create(String iso31661, String name) {
        return builder()
                .iso31661(iso31661)
                .name(name)
                .build();
    }

    public static Builder builder() {
        return new $AutoValue_ProductionCountry.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder iso31661(String iso31661);

        public abstract Builder name(String name);

        public abstract ProductionCountry build();
    }

    public static TypeAdapter<ProductionCountry> typeAdapter(Gson gson) {
        return new AutoValue_ProductionCountry.GsonTypeAdapter(gson);
    }
}
