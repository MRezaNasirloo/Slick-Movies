package com.github.pedramrn.slick.parent.ui.favorite.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.favorite.ViewStateFavorite;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public class FavoriteListNoOp implements PartialViewState<ViewStateFavorite> {

    @Override
    public ViewStateFavorite reduce(ViewStateFavorite state) {
        return state;
    }
}
