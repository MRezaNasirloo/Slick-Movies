package com.github.pedramrn.slick.parent.ui.details.favorite;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-11
 */
public interface ViewFloatingFavorite {
    void setFavorite(boolean isFavorite);

    @NonNull
    Observable<Boolean> commandFavorite();

    @NonNull
    Observable<MovieBasic> movie();

    void setMovie(MovieBasic movie);
}
