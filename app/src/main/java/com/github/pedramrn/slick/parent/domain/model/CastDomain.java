package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class CastDomain {

    public abstract Integer id();

    public abstract Integer castId();

    public abstract String creditId();

    public abstract String name();

    @Nullable
    public abstract String profilePath();

    public abstract String character();

    public abstract Integer gender();

    public abstract Integer order();

    public static CastDomain create(Integer id, Integer castId, String creditId, String name, String profilePath, String character, Integer gender,
                                    Integer order) {
        return new AutoValue_CastDomain(id, castId, creditId, name, profilePath, character, gender, order);
    }


}
