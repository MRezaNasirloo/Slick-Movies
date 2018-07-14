package com.github.pedramrn.slick.parent.ui.videos.state;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.util.Utils;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.Iterator;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-03-06
 */
public class Error implements PartialViewState<ViewStateVideos> {

    public static final String VIDEOS = "VIDEOS";
    private final Throwable throwable;

    public Error(Throwable throwable) {
        this.throwable = throwable;
    }

    @NonNull
    @Override
    public ViewStateVideos reduce(@NonNull ViewStateVideos state) {
        if (state.error() == null) {
            Iterator<Item> iterator = state.videosItem().iterator();
            Utils.removeRemovables(iterator, VIDEOS);
            state = state.toBuilder().error(throwable).build();
        }
        return state;
    }
}
