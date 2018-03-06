package com.github.pedramrn.slick.parent.ui.videos.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class Videos implements PartialViewState<ViewStateVideos> {
    private final List<Item> videos;

    public Videos(List<Item> videos) {
        this.videos = videos;
    }

    @Override
    public ViewStateVideos reduce(ViewStateVideos state) {
        return state.toBuilder().videos(videos).build();
    }
}
