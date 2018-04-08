package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.home.cardlist.ViewStateCardList;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class Movies implements PartialViewState<ViewStateCardList> {

    private final Map<Integer, Item> newMovies;

    public Movies(Map<Integer, Item> newMovies) {
        this.newMovies = newMovies;
    }

    @Override
    public ViewStateCardList reduce(ViewStateCardList state) {
        System.out.println("Movies.reduce newMovies size: " + newMovies.size() + " " + newMovies);
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
//        removeRemovables(movies.values().iterator());
        movies.putAll(newMovies);
        return state.toBuilder()
                .movies(movies)
                .itemLoadedCount(movies.size())
                .error(null)
                .build();
    }
}
