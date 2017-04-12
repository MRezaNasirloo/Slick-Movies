package com.github.pedramrn.slick.parent.datasource.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */

public class Rating {

    @SerializedName("Source")
    @Expose
    public String source;
    @SerializedName("Value")
    @Expose
    public String value;

}
