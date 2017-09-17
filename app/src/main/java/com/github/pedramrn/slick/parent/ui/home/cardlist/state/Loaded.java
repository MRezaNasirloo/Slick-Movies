package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.cardlist.ViewStateCardList;
import com.xwray.groupie.Item;

import java.util.Map;
import java.util.TreeMap;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class Loaded implements PartialViewState<ViewStateCardList> {

    private final boolean loading;

    public Loaded(boolean loaded) {
        this.loading = !loaded;

    }

    @Override
    public ViewStateCardList reduce(ViewStateCardList state) {
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
        removeRemovables(movies.values().iterator());
        return state.toBuilder()
                .movies(movies)
                .isLoading(loading)
                .itemLoadedCount(movies.size())
                .page(state.page() + 1)
                .error(null)
                .build();
    }
}
