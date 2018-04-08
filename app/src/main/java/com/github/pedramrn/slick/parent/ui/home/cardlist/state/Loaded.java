package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.home.cardlist.ViewStateCardList;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.Map;
import java.util.TreeMap;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class Loaded implements PartialViewState<ViewStateCardList> {

    @Override
    public ViewStateCardList reduce(ViewStateCardList state) {
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
        removeRemovables(movies.values().iterator(), null);
        return state.toBuilder()
                .movies(movies)
                .isLoading(false)
                .itemLoadedCount(movies.size())
                .page(state.page() + 1)
                .error(null)
                .build();
    }
}
