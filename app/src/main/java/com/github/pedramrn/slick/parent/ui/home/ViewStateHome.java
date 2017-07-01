package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */
@AutoValue
abstract class ViewStateHome {
    @Nullable
    public abstract List<ItemVideo> items();

    @Nullable
    public abstract List<String> foo();

    @Nullable
    public abstract List<Video> videos();

    @Nullable
    public abstract List<Movie> movies();

    @Nullable
    public abstract List<ItemCard> trending();

    @Nullable
    public abstract Throwable videosError();

    @Nullable
    public abstract Throwable trendingError();


    public abstract Builder toBuilder();

    public static ViewStateHome create(List<ItemVideo> items, List<String> foo, List<Video> videos, List<Movie> movies, List<ItemCard> trending,
                                       Throwable videosError, Throwable trendingError) {
        return builder()
                .items(items)
                .foo(foo)
                .videos(videos)
                .movies(movies)
                .trending(trending)
                .videosError(videosError)
                .trendingError(trendingError)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateHome.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder items(List<ItemVideo> items);

        public abstract Builder foo(List<String> foo);

        public abstract Builder videos(List<Video> videos);

        public abstract Builder movies(List<Movie> movies);

        public abstract Builder trending(List<ItemCard> trending);

        public abstract Builder videosError(Throwable videosError);

        public abstract Builder trendingError(Throwable trendingError);

        public abstract ViewStateHome build();
    }
}
