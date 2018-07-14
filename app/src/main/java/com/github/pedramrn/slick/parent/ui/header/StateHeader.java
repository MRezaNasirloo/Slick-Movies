package com.github.pedramrn.slick.parent.ui.header;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.videos.state.AutoValue_StateHeader;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-07-22
 */

@AutoValue
public abstract class StateHeader {
    public abstract Builder movie(MovieBasic movie);

    public abstract Item header();

    @Nullable
    public abstract Throwable error();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_StateHeader.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder movie(MovieBasic movie);

        public abstract Builder header(Item header);

        public abstract Builder error(Throwable errorVideos);

        public abstract StateHeader build();

    }
}
