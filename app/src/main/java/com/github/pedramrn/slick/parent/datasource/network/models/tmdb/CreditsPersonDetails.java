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
public abstract class CreditsPersonDetails {

    @SerializedName("cast")
    public abstract List<CastOrCrewPersonDetailsTmdb> cast();

    @SerializedName("crew")
    public abstract List<CastOrCrewPersonDetailsTmdb> crew();

    public static TypeAdapter<CreditsPersonDetails> typeAdapter(Gson gson) {
        return new AutoValue_CreditsPersonDetails.GsonTypeAdapter(gson);
    }
}
