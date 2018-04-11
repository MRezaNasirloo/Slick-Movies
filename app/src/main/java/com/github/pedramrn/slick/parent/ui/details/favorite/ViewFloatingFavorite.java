package com.github.pedramrn.slick.parent.ui.details.favorite;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-11
 */
public interface ViewFloatingFavorite {
    void setFavorite(boolean isFavorite);

    Observable<Boolean> commandFavorite();

    Observable<MovieBasic> movie();

    void setMovie(MovieBasic movie);
}
