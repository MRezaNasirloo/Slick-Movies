package com.github.pedramrn.slick.parent.ui.favorite.state;

import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.favorite.ViewStateFavorite;
import com.github.pedramrn.slick.parent.ui.favorite.item.ItemFavoriteProgressive;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.Map;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public class FavoriteListProgressive extends PartialProgressive implements PartialViewState<ViewStateFavorite> {


    public FavoriteListProgressive() {
        super(2, MovieSmall.FAVORITE, (id, tag) -> new ItemFavoriteProgressive(id));
    }

    @Override
    public ViewStateFavorite reduce(ViewStateFavorite state) {
        Map<Integer, Item> favorites = state.favorites();
        removeRemovables(favorites.values().iterator(), "Details");
        return state.toBuilder().favorites(reduce(favorites)).build();
    }
}
