package com.github.pedramrn.slick.parent.ui.details.model;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Image {


    public abstract Integer id();

    public abstract List<String> backdrops();

    public abstract List<String> posters();

    public static Image create(Integer id, List<String> backdrops, List<String> posters) {
        return new AutoValue_Image(id, backdrops, posters);
    }


}
