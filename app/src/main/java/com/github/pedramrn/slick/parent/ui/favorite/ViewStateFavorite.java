package com.github.pedramrn.slick.parent.ui.favorite;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

@AutoValue
public abstract class ViewStateFavorite {
    public abstract Map<Integer, Item> favorites();
    @Nullable
    public abstract Throwable errorFavorites();
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ViewStateFavorite.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder favorites(Map<Integer, Item> favorites);

        public abstract Builder errorFavorites(Throwable errorFavorites);

        public abstract ViewStateFavorite build();
    }
}
