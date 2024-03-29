package com.github.pedramrn.slick.parent.domain.model;

import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class ImageDomain {

    public abstract List<String> backdrops();

    public abstract List<String> posters();

    public static ImageDomain create(List<String> backdrops, List<String> posters) {
        return new AutoValue_ImageDomain(backdrops, posters);
    }


}
