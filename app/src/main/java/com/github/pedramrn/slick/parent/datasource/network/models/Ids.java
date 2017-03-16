package com.github.pedramrn.slick.parent.datasource.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public class Ids {

    @SerializedName("trakt")
    @Expose
    public Integer trakt;
    @SerializedName("slug")
    @Expose
    public String slug;
    @SerializedName("imdb")
    @Expose
    public String imdb;
    @SerializedName("tmdb")
    @Expose
    public Integer tmdb;

    @Override
    public String toString() {
        return "Ids{" +
                "trakt=" + trakt +
                ", slug='" + slug + '\'' +
                ", imdb='" + imdb + '\'' +
                ", tmdb=" + tmdb +
                '}';
    }
}
