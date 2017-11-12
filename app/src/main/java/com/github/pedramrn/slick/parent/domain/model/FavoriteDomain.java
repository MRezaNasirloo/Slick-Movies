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

    public abstract Builder toBuilder();

    public static FavoriteDomain create(String imdbId, Integer tmdb, String name, String type) {
        return builder()
                .imdbId(imdbId)
                .tmdb(tmdb)
                .name(name)
                .type(type)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_FavoriteDomain.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder imdbId(String imdbId);

        public abstract Builder tmdb(Integer tmdb);

        public abstract Builder name(String name);

        public abstract Builder type(String type);

        public abstract FavoriteDomain build();
    }
}
