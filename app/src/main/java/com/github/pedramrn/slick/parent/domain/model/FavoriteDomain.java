package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

@AutoValue
public abstract class FavoriteDomain {

    @Nullable
    public abstract String imdbId();

    public abstract Integer tmdb();

    public abstract String name();

    public abstract String type();

    public static FavoriteDomain create(String imdbId, Integer tmdb, String name, String type) {
        return new AutoValue_FavoriteDomain(imdbId, tmdb, name, type);
    }

}
