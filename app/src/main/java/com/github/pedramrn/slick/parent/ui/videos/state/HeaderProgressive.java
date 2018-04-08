package com.github.pedramrn.slick.parent.ui.videos.state;

import com.github.pedramrn.slick.parent.ui.favorite.item.ItemFavoriteProgressive;
import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class HeaderProgressive implements PartialViewState<ViewStateVideos> {

    @Override
    public ViewStateVideos reduce(ViewStateVideos state) {
        return state.toBuilder().header(new ItemFavoriteProgressive(0)).build();
    }
}
