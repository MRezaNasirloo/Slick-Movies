package com.github.pedramrn.slick.parent.ui.videos.state;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-07-22
 */

@AutoValue
public abstract class ViewStateVideos {

    public abstract List<Item> videosItem();

    public abstract List<Video> videos();

    @Nullable
    public abstract Throwable error();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ViewStateVideos.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder videosItem(List<Item> videos);

        public abstract Builder videos(List<Video> videos);

        public abstract Builder error(Throwable errorVideos);

        public abstract ViewStateVideos build();
    }
}
