package com.github.pedramrn.slick.parent.ui.details;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

@AutoValue
abstract class ViewStateDetails {

    public abstract List<Item> similar();

    public abstract List<Item> casts();

    public abstract List<Item> backdrops();

    public abstract List<Item> comments();

    public abstract MovieBasic movieBasic();

    @Nullable
    public abstract Boolean isFavorite();

    @Nullable
    public abstract Throwable error();

    @Nullable
    public abstract Throwable errorFavorite();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ViewStateDetails.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder similar(List<Item> similar);

        public abstract Builder movieBasic(MovieBasic movieBasic);

        public abstract Builder casts(List<Item> cast);

        public abstract Builder backdrops(List<Item> backdrops);

        public abstract Builder comments(List<Item> comments);

        public abstract Builder isFavorite(Boolean isFavorite);

        public abstract Builder errorFavorite(Throwable errorFavorite);

        public abstract Builder error(Throwable error);

        public abstract ViewStateDetails build();
    }
}
