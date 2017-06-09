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
public abstract class Credit {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("cast")
    public abstract List<Cast> cast();

    @SerializedName("crew")
    public abstract List<Crew> crew();

    public static Credit create(Integer id, List<Cast> cast, List<Crew> crew) {
        return builder()
                .id(id)
                .cast(cast)
                .crew(crew)
                .build();
    }


    public static TypeAdapter<Credit> typeAdapter(Gson gson) {
        return new AutoValue_Credit.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_Credit.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder cast(List<Cast> cast);

        public abstract Builder crew(List<Crew> crew);

        public abstract Credit build();
    }
}
