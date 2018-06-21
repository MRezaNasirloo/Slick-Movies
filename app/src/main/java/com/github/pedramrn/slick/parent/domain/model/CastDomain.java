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

    @Nullable
    public abstract Integer castId();

    @Nullable
    public abstract String creditId();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String profilePath();

    @Nullable
    public abstract String character();

    @Nullable
    public abstract Integer gender();

    @Nullable
    public abstract Integer order();

    public static CastDomain create(Integer id, Integer castId, String creditId, String name, String profilePath, String character, Integer gender,
                                    Integer order) {
        return new AutoValue_CastDomain(id, castId, creditId, name, profilePath, character, gender, order);
    }


}
