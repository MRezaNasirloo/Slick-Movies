package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdrop;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCast;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.util.Indexed;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

class ViewStateDetails {
    private final Movie movie;

    public ViewStateDetails(Movie movie) {
        this.movie = movie;
    }

    public Movie movieDetails() {
        return movie;
    }

    public List<ItemCast> itemCasts() {
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
}
