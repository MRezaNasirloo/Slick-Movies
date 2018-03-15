package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-27
 */

@AutoValue
public abstract class CastOrCrewPersonDetailsTmdb {

    //crew/cast data

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("credit_id")
    public abstract String creditId();

    /**
     * NOTE: for casts only, otherwise null will be returned
     */
    @Nullable
    @SerializedName("character")
    public abstract String character();

    /**
     * NOTE: for crews only, otherwise null will be returned
     */
    @Nullable
    @SerializedName("job")
    public abstract String job();

    /**
     * NOTE: for crews only, otherwise null will be returned
     */
    @Nullable
    @SerializedName("department")
    public abstract String department();

    /**
     * NOTE: for TV shows only, otherwise returns null
     *
     * @return the episode counts that this crew/cast has took part in it.
     */
    @Nullable
    @SerializedName("episode_count")
    public abstract Integer episodeCount();

    //movie or tv data

    /**
     * NOTE: for TV shows only, otherwise returns null
     */
    @Nullable
    @SerializedName("name")
    public abstract String name();

    /**
     * NOTE: for TV shows only, otherwise returns null
     */
    @Nullable
    @SerializedName("original_name")
    public abstract String originalName();

    /**
     * NOTE: for Movies only, otherwise returns null
     */
    @Nullable
    @SerializedName("title")
    public abstract String title();

    /**
     * NOTE: for Movies only, otherwise returns null
     */
    @Nullable
    @SerializedName("original_title")
    public abstract String originalTitle();

    @Nullable
    @SerializedName("overview")
    public abstract String overview();

    @Nullable
    @SerializedName("poster_path")
    public abstract String posterPath();

    @Nullable
    @SerializedName("backdrop_path")
    public abstract String backdropPath();

    /**
     * NOTE: for TV shows only, otherwise returns null
     */
    @Nullable
    @SerializedName("first_air_date")
    public abstract String firstAirDate();

    /**
     * NOTE: for Movies only, otherwise returns null
     */
    @Nullable
    @SerializedName("release_date")
    public abstract String releaseDate();

    @Nullable
    @SerializedName("original_language")
    public abstract String originalLanguage();

    @Nullable
    @SerializedName("adult")
    public abstract Boolean adult();

    @SerializedName("origin_country")
    public abstract List<String> originCountry();

    @SerializedName("genre_ids")
    public abstract List<Integer> genreIds();

    @Nullable
    @SerializedName("popularity")
    public abstract Float popularity();

    @Nullable
    @SerializedName("vote_count")
    public abstract Integer voteCount();

    @Nullable
    @SerializedName("vote_average")
    public abstract Float voteAverage();

    public static TypeAdapter<CastOrCrewPersonDetailsTmdb> typeAdapter(Gson gson) {
        return new AutoValue_CastOrCrewPersonDetailsTmdb.GsonTypeAdapter(gson);
    }
}
