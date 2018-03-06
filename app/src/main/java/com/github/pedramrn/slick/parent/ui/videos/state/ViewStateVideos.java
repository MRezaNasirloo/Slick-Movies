package com.github.pedramrn.slick.parent.ui.videos.state;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-22
 */

@AutoValue
public abstract class ViewStateVideos {

    public abstract List<Item> videos();

    public abstract Item header();

    @Nullable
    public abstract Throwable error();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ViewStateVideos.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder videos(List<Item> videos);

        public abstract Builder error(Throwable errorVideos);

        public abstract Builder header(Item header);

        public abstract ViewStateVideos build();
    }
}
