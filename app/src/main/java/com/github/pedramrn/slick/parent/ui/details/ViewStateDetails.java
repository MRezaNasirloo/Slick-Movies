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
    public abstract Throwable errorSimilar();

    @Nullable
    public abstract Throwable errorMovieCast();

    @Nullable
    public abstract Throwable errorMovieBackdrop();

    @Nullable
    public abstract Throwable errorMovie();

    @Nullable
    public abstract Throwable errorComments();

    /*@Nullable
    public Throwable errorSimilarConsume(){
        Throwable throwable = errorSimilar();
    }

    @Nullable
    public Throwable errorMovieCastConsume(){
        Throwable throwable = errorMovieCast();
    }

    @Nullable
    public Throwable errorMovieBackdropConsume(){
        Throwable throwable = errorMovieBackdrop();
    }

    @Nullable
    public Throwable errorMovieConsume(){
        Throwable throwable = errorMovie();
    }

    @Nullable
    public Throwable errorCommentsConsume(){
        Throwable throwable = errorComments();
    }*/

    public abstract Builder toBuilder();

    public static ViewStateDetails create(List<Item> similar, List<Item> casts, List<Item> backdrops, List<Item> comments, MovieBasic movieBasic,
                                          Throwable errorSimilar, Throwable errorMovieCast, Throwable errorMovieBackdrop, Throwable errorMovie,
                                          Throwable errorComments) {
        return builder()
                .similar(similar)
                .casts(casts)
                .backdrops(backdrops)
                .comments(comments)
                .movieBasic(movieBasic)
                .errorSimilar(errorSimilar)
                .errorMovieCast(errorMovieCast)
                .errorMovieBackdrop(errorMovieBackdrop)
                .errorMovie(errorMovie)
                .errorComments(errorComments)
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

        public abstract Builder comments(List<Item> comments);

        public abstract Builder errorComments(Throwable errorComments);

        public abstract ViewStateDetails build();
    }
}
