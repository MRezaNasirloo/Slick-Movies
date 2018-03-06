package com.github.pedramrn.slick.parent.ui.videos.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class Header implements PartialViewState<ViewStateVideos> {
    private final Item movie;

    public Header(Item movie) {
        this.movie = movie;
    }

    @Override
    public ViewStateVideos reduce(ViewStateVideos state) {
        return state.toBuilder().header(movie).build();
    }
}
