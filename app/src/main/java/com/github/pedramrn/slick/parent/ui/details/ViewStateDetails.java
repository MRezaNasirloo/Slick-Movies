package com.github.pedramrn.slick.parent.ui.details;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdrop;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCast;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.util.Indexed;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

@AutoValue
abstract class ViewStateDetails {
    @Nullable
    public abstract Movie movie();

    public abstract List<Item> similar();

    @Nullable
    public abstract Throwable errorSimilar();

    @Nullable
    public abstract Throwable errorMovie();

    public abstract Builder toBuilder();

    public List<ItemCast> itemCasts() {
        Movie movie = movie();
        if (movie == null) {
            return Collections.emptyList();
        }
        final List<Cast> casts = movie.casts();

        final List<ItemCast> itemCasts = Observable.fromIterable(casts)
                .zipWith(Observable.range(0, casts.size()), new BiFunction<Cast, Integer, Indexed<Cast>>() {
                    @Override
                    public Indexed<Cast> apply(@io.reactivex.annotations.NonNull Cast cast, @io.reactivex.annotations.NonNull Integer index)
                            throws Exception {
                        return new Indexed<>(cast, index);
                    }
                })
                .map(new Function<Indexed<Cast>, ItemCast>() {
                    @Override
                    public ItemCast apply(@io.reactivex.annotations.NonNull Indexed<Cast> cast) throws Exception {
                        return new ItemCast(cast.index(), cast.value());
                    }
                }).toList().blockingGet();

        casts.clear();

        return itemCasts;
    }

    public List<ItemBackdrop> itemBackdrops() {
        Movie movie = movie();
        if (movie == null) {
            return Collections.emptyList();
        }
        List<Backdrop> backdrops = movie.images().backdrops();

        List<ItemBackdrop> itemBackdrops = Observable.fromIterable(backdrops)
                .zipWith(Observable.range(0, backdrops.size()), new BiFunction<Backdrop, Integer, Indexed<Backdrop>>() {
                    @Override
                    public Indexed<Backdrop> apply(@io.reactivex.annotations.NonNull Backdrop backdrop,
                                                   @io.reactivex.annotations.NonNull Integer index)
                            throws Exception {
                        return new Indexed<>(backdrop, index);
                    }
                })
                .map(new Function<Indexed<Backdrop>, ItemBackdrop>() {
                    @Override
                    public ItemBackdrop apply(@io.reactivex.annotations.NonNull Indexed<Backdrop> backdropIndexed) throws Exception {
                        return new ItemBackdrop(backdropIndexed.index(), backdropIndexed.value());
                    }
                }).toList().blockingGet();

        backdrops.clear();

        return itemBackdrops;
    }

    public static ViewStateDetails create(Movie movie, List<Item> similar, Throwable errorSimilar, Throwable errorMovie) {
        return builder()
                .movie(movie)
                .similar(similar)
                .errorSimilar(errorSimilar)
                .errorMovie(errorMovie)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateDetails.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder movie(Movie movie);

        public abstract Builder similar(List<Item> similar);

        public abstract Builder errorSimilar(Throwable errorSimilar);

        public abstract Builder errorMovie(Throwable errorMovie);

        public abstract ViewStateDetails build();
    }
}
