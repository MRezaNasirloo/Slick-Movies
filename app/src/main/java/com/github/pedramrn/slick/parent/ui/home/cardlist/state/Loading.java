package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.cardlist.ViewStateCardList;
import com.github.pedramrn.slick.parent.ui.home.item.ItemLoading;
import com.xwray.groupie.Item;

import java.util.Map;
import java.util.TreeMap;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class Loading implements PartialViewState<ViewStateCardList> {

    public static final String TAG = "LOADING_CARD_LIST";

    @Override
    public ViewStateCardList reduce(ViewStateCardList state) {
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
        removeRemovables(movies.values().iterator(), TAG);
        movies.put(movies.size(), new ItemLoading(Long.MAX_VALUE));
        return state.toBuilder()
                .isLoading(true)
                .movies(movies)
                .itemLoadedCount(movies.size())
                .error(null)
                .build();
    }
}
