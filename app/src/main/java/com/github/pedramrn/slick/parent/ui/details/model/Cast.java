package com.github.pedramrn.slick.parent.ui.details.model;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Cast {

    public abstract Integer id();

    public abstract Integer castId();

    public abstract String creditId();

    public abstract String name();

    public abstract String profilePath();

    public abstract String character();

    public abstract Integer gender();

    public abstract Integer order();

    public static Cast create(Integer id, Integer castId, String creditId, String name, String profilePath, String character, Integer gender,
                              Integer order) {
        return new AutoValue_Cast(id, castId, creditId, name, profilePath, character, gender, order);
    }


}
