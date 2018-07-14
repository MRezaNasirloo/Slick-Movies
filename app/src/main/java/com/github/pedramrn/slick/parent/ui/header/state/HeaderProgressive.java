package com.github.pedramrn.slick.parent.ui.header.state;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.favorite.item.ItemFavoriteProgressive;
import com.github.pedramrn.slick.parent.ui.header.StateHeader;
import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class HeaderProgressive implements PartialViewState<StateHeader> {

    @NonNull
    @Override
    public StateHeader reduce(@NonNull StateHeader state) {
        return state.toBuilder().header(new ItemFavoriteProgressive(0)).build();
    }
}
