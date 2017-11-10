package com.github.pedramrn.slick.parent.ui.favorite.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.favorite.ViewStateFavorite;
import com.xwray.groupie.Item;

import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public class FavoriteList implements PartialViewState<ViewStateFavorite> {
    private final Map<Integer, Item> favoriteMap;

    public FavoriteList(Map<Integer, Item> favoriteMap) {
        this.favoriteMap = favoriteMap;
    }

    @Override
    public ViewStateFavorite reduce(ViewStateFavorite state) {
        return state.toBuilder().favorites(favoriteMap).build();
    }
}
