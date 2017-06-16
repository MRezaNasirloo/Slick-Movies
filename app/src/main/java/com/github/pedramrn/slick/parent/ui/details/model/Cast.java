package com.github.pedramrn.slick.parent.ui.details.model;

import android.support.annotation.Nullable;

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

    @Nullable
    protected abstract String profilePath();

    public abstract String character();

    public abstract Integer gender();

    public abstract Integer order();

    @Nullable
    public String profileIcon() {
        if (profilePath() == null) return null;
        return "http://image.tmdb.org/t/p/w92" + profilePath();
    }

    @Nullable
    public String profileOriginal() {
        if (profilePath() == null) return null;
        return "http://image.tmdb.org/t/p/original" + profilePath();
    }

    public static Cast create(Integer id, Integer castId, String creditId, String name, String profilePath, String character, Integer gender,
                              Integer order) {
        return new AutoValue_Cast(id, castId, creditId, name, profilePath, character, gender, order);
    }


}
