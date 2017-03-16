package com.github.pedramrn.slick.parent.datasource.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public class Movie {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("year")
    @Expose
    public Integer year;
    @SerializedName("ids")
    @Expose
    public Ids ids;

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", ids=" + ids +
                '}';
    }
}
