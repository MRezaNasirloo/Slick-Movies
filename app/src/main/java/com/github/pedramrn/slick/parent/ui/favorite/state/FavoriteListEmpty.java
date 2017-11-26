package com.github.pedramrn.slick.parent.ui.favorite.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.favorite.ViewStateFavorite;
import com.github.pedramrn.slick.parent.ui.favorite.item.ItemEmptyStateFullScreen;
import com.github.pedramrn.slick.parent.ui.favorite.item.ItemFavorite;
import com.github.pedramrn.slick.parent.util.Utils;
import com.xwray.groupie.Item;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public class FavoriteListEmpty implements PartialViewState<ViewStateFavorite> {

    private final boolean hadError;
    private final boolean forceClear;

    public FavoriteListEmpty(boolean hadError, boolean forceClear) {
        this.hadError = hadError;
        this.forceClear = forceClear;
    }

    @Override
    public ViewStateFavorite reduce(ViewStateFavorite state) {
        if (forceClear) return clear(state, ItemFavorite.FORCE);
        if (!hadError) {
            return clear(state, Movie.FAVORITE);
        }
        return state;
    }

    protected ViewStateFavorite clear(ViewStateFavorite state, String tag) {
        Map<Integer, Item> favorites = state.favorites();
        favorites = new TreeMap<>(favorites);
        Utils.removeRemovables(favorites.values().iterator(), tag);
        if (favorites.isEmpty()) favorites.put(0, new ItemEmptyStateFullScreen());
        return state.toBuilder().favorites(favorites).build();
    }
}
