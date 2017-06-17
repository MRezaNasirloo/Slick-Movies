package com.github.pedramrn.slick.parent.ui.details.model;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Image {

    public abstract List<Backdrop> backdrops();

    public abstract List<String> posters();

    public static Image create(List<Backdrop> backdrops, List<String> posters) {
        return new AutoValue_Image(backdrops, posters);
    }


}
