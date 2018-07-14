package com.github.pedramrn.slick.parent.ui.videos.state;

import android.support.annotation.NonNull;

import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class ErrorDismissed implements PartialViewState<ViewStateVideos> {

    @NonNull
    @Override
    public ViewStateVideos reduce(@NonNull ViewStateVideos state) {
        return state.toBuilder().error(null).build();
    }
}
