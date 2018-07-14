package com.github.pedramrn.slick.parent.ui.videos.state;

import android.support.annotation.NonNull;

import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-03-06
 */
public class VideosItem implements PartialViewState<ViewStateVideos> {
    private final List<Item> videos;

    public VideosItem(List<Item> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewStateVideos reduce(@NonNull ViewStateVideos state) {
        return state.toBuilder().videosItem(videos).build();
    }
}
