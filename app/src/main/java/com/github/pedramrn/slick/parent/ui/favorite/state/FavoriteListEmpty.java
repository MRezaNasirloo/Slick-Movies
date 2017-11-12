package com.github.pedramrn.slick.parent.ui.favorite.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.favorite.ViewStateFavorite;

import java.util.Collections;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public class FavoriteListEmpty implements PartialViewState<ViewStateFavorite> {

    public FavoriteListEmpty() {
    }

    @Override
    public ViewStateFavorite reduce(ViewStateFavorite state) {
        return state.toBuilder().favorites(Collections.emptyMap()).build();
    }
}
