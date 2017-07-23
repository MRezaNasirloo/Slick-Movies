package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */
@AutoValue
public abstract class CommentTrakt {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("comment")
    public abstract String comment();

    @SerializedName("spoiler")
    public abstract Boolean spoiler();

    @SerializedName("review")
    public abstract Boolean review();

    @SerializedName("parent_id")
    public abstract Integer parentId();

    @SerializedName("replies")
    public abstract Integer replies();

    @SerializedName("likes")
    public abstract Integer likes();

    @SerializedName("user_rating")
    public abstract Integer userRating();

    @SerializedName("user")
    public abstract UserTrakt user();

    @SerializedName("created_at")
    public abstract String createdAt();

    @SerializedName("updated_at")
    public abstract String updatedAt();

    public static TypeAdapter<CommentTrakt> typeAdapter(Gson gson) {
        return new AutoValue_CommentTrakt.GsonTypeAdapter(gson);
    }

}
