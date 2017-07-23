package com.github.pedramrn.slick.parent.datasource.network.models.trakt;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.ryanharter.auto.value.gson.GsonTypeAdapter;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

@AutoValue
public abstract class UserTrakt {
    @SerializedName("username")
    public abstract String username();

    // @SerializedName("private")
    // public abstract Boolean isPrivate();

    @SerializedName("name")
    public abstract String name();

    // @SerializedName("vip")
    // public abstract Boolean vip();

    // @SerializedName("vip_ep")
    // public abstract Boolean vipEp();

    @SerializedName("ids")
    @GsonTypeAdapter(TypeAdapterIdsToString.class)
    public abstract String id();

    public static TypeAdapter<UserTrakt> typeAdapter(Gson gson) {
        return new AutoValue_UserTrakt.GsonTypeAdapter(gson);
    }
}
