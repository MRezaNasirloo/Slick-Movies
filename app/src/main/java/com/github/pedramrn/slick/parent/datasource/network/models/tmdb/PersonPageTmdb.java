package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */

@AutoValue
public abstract class PersonPageTmdb {

    @SerializedName("page")
    public abstract Integer page();

    @SerializedName("total_pages")
    public abstract Integer totalPages();

    @SerializedName("total_results")
    public abstract Integer totalResults();

    @SerializedName("results")
    public abstract List<PersonTmdb> persons();

    public static TypeAdapter<PersonPageTmdb> typeAdapter(Gson gson) {
        return new AutoValue_PersonPageTmdb.GsonTypeAdapter(gson);
    }

}
