package com.github.pedramrn.slick.parent.ui.favorite.state;

import com.github.pedramrn.slick.parent.ui.boxoffice.item.ItemBoxOfficeError;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.favorite.ViewStateFavorite;
import com.github.pedramrn.slick.parent.util.Utils;
import com.xwray.groupie.Item;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public class FavoriteListError implements PartialViewState<ViewStateFavorite> {

    private final Throwable throwable;

    public FavoriteListError(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ViewStateFavorite reduce(ViewStateFavorite state) {
        Map<Integer, Item> favorites = new TreeMap<>(state.favorites());
        Iterator<Item> iterator = favorites.values().iterator();
        Utils.removeRemovables(iterator, "Favorites");
        favorites.put(favorites.size(), new ItemBoxOfficeError(favorites.size(), ErrorHandler.handle(throwable)));
        return state.toBuilder().errorFavorites(throwable).favorites(favorites).build();
    }
}