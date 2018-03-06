package com.github.pedramrn.slick.parent.ui.videos.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.util.Utils;
import com.xwray.groupie.Item;

import java.util.Iterator;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class Error implements PartialViewState<ViewStateVideos> {

    public static final String VIDEOS = "VIDEOS";
    private final Throwable throwable;

    public Error(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ViewStateVideos reduce(ViewStateVideos state) {
        if (state.error() == null) {
            Iterator<Item> iterator = state.videos().iterator();
            Utils.removeRemovables(iterator, VIDEOS);
            state = state.toBuilder().error(throwable).build();
        }
        return state;
    }
}
