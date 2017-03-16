package com.github.pedramrn.slick.parent.datasource.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public class BoxOfficeItem {
    @SerializedName("revenue")
    @Expose
    public Integer revenue;
    @SerializedName("movie")
    @Expose
    public Movie movie;

    @Override
    public String toString() {
        return "BoxOfficeItem{" +
                "revenue=" + revenue +
                ", movie=" + movie +
                '}';
    }
}
