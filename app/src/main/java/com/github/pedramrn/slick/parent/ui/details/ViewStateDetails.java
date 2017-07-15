package com.github.pedramrn.slick.parent.ui.details;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.model.Movie;
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

    public abstract MovieBasic movieBasic();

    @Nullable
    public abstract Throwable errorSimilar();

    @Nullable
    public abstract Throwable errorMovieCast();

    @Nullable
    public abstract Throwable errorMovieBackdrop();

    @Nullable
    public abstract Throwable errorMovie();

    public abstract Builder toBuilder();

    public static ViewStateDetails create(List<Item> similar, List<Item> cast, List<Item> backdrops, MovieBasic movieBasic,
                                          Throwable errorSimilar, Throwable errorMovieCast, Throwable errorMovieBackdrop, Throwable errorMovie) {
        return builder()
                .similar(similar)
                .casts(cast)
                .backdrops(backdrops)
                .movieBasic(movieBasic)
                .errorSimilar(errorSimilar)
                .errorMovieCast(errorMovieCast)
                .errorMovieBackdrop(errorMovieBackdrop)
                .errorMovie(errorMovie)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateDetails.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder similar(List<Item> similar);

        public abstract Builder errorSimilar(Throwable errorSimilar);

        public abstract Builder errorMovie(Throwable errorMovie);

        public abstract Builder movieBasic(MovieBasic movieBasic);

        public abstract Builder casts(List<Item> cast);

        public abstract Builder backdrops(List<Item> backdrops);

        public abstract Builder errorMovieCast(Throwable errorMovieCast);

        public abstract Builder errorMovieBackdrop(Throwable errorMovieBackdrop);

        public abstract ViewStateDetails build();
    }
}
