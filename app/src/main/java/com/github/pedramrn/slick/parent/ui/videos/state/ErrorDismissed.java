package com.github.pedramrn.slick.parent.ui.videos.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class ErrorDismissed implements PartialViewState<ViewStateVideos> {

    @Override
    public ViewStateVideos reduce(ViewStateVideos state) {
        return state.toBuilder().error(null).build();
    }
}
