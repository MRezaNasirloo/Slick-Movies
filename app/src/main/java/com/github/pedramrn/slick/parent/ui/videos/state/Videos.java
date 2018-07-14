package com.github.pedramrn.slick.parent.ui.videos.state;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.mrezanasirloo.slick.uni.PartialViewState;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class Videos implements PartialViewState<ViewStateVideos> {
    private final List<Video> videos;

    public Videos(List<Video> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewStateVideos reduce(@NonNull ViewStateVideos state) {
        return state.toBuilder().videos(videos).build();
    }
}
