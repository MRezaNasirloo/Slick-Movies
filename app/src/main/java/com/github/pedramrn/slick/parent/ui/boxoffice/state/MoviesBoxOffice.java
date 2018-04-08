package com.github.pedramrn.slick.parent.ui.boxoffice.state;

import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.Map;
import java.util.TreeMap;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-24
 *         TODO: create other state for box office: Loading, Loaded.
 */

public class MoviesBoxOffice implements PartialViewState<ViewStateBoxOffice> {

    private final Map<Integer, Item> movies;

    public MoviesBoxOffice(Map<Integer, Item> movies) {
        this.movies = movies;
    }

    private static final String TAG = MoviesBoxOffice.class.getSimpleName();

    @Override
    public ViewStateBoxOffice reduce(ViewStateBoxOffice state) {
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
        removeRemovables(movies.values().iterator(), null);
        movies.putAll(this.movies);
        return state.toBuilder().movies(movies).error(null).build();
    }
}
