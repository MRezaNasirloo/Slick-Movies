package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-12
 */

@AutoValue
public abstract class HomeViewState {
    public abstract List<MovieItem> movieItems();

    public static HomeViewState create(List<MovieItem> movieItems) {
        return new AutoValue_HomeViewState(movieItems);
    }

}
