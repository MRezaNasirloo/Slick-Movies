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
public abstract class ProductionCompany implements SimpleData {

    @SerializedName("name")
    public abstract String name();

    @SerializedName("id")
    public abstract Integer id();

    @Override
    public String get() {
        return name();
    }

    public static ProductionCompany create(String name, Integer id) {
        return builder()
                .name(name)
                .id(id)
                .build();
    }

    public static TypeAdapter<ProductionCompany> typeAdapter(Gson gson) {
        return new AutoValue_ProductionCompany.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_ProductionCompany.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder name(String name);

        public abstract Builder id(Integer id);

        public abstract ProductionCompany build();
    }
}
