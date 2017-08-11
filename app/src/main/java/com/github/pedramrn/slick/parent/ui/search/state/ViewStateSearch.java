package com.github.pedramrn.slick.parent.ui.search.state;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

@AutoValue
public abstract class ViewStateSearch {
    public abstract List<Item> movies();

    @Nullable
    public abstract Throwable errorMovies();

    public abstract boolean loadingMovies();

    public static Builder builder() {
        return new AutoValue_ViewStateSearch.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder movies(List<Item> movies);

        public abstract Builder errorMovies(Throwable errorMovies);

        public abstract Builder loadingMovies(boolean loadingMovies);

        public abstract ViewStateSearch build();
    }
}
