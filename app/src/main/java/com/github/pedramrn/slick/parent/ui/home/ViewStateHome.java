package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.google.auto.value.AutoValue;

import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */
@AutoValue
abstract class ViewStateHome {
    @Nullable
    public abstract Map<Integer, ItemVideo> items();

    @Nullable
    public abstract Map<Integer, String> foo();

    @Nullable
    public abstract Map<Integer, Video> videos();

    @Nullable
    public abstract Map<Integer, Movie> movies();

    @Nullable
    public abstract Map<Integer, ItemCard> trending();

    public abstract boolean loadingTrending();

    public abstract int itemLoadingCount();

    @Nullable
    public abstract Map<Integer, ItemCard> popular();

    @Nullable
    public abstract Throwable videosError();

    @Nullable
    public abstract Throwable error();


    public abstract Builder toBuilder();

    public static ViewStateHome create(Map<Integer, ItemVideo> items, Map<Integer, String> foo, Map<Integer, Video> videos,
                                       Map<Integer, Movie> movies,
                                       Map<Integer, ItemCard> trending, boolean loadingTrending, int itemLoadingCount, Map<Integer, ItemCard> popular,
                                       Throwable videosError, Throwable error) {
        return builder()
                .items(items)
                .foo(foo)
                .videos(videos)
                .movies(movies)
                .trending(trending)
                .loadingTrending(loadingTrending)
                .itemLoadingCount(itemLoadingCount)
                .popular(popular)
                .videosError(videosError)
                .error(error)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateHome.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder items(Map<Integer, ItemVideo> items);

        public abstract Builder foo(Map<Integer, String> foo);

        public abstract Builder videos(Map<Integer, Video> videos);

        public abstract Builder movies(Map<Integer, Movie> movies);

        public abstract Builder trending(Map<Integer, ItemCard> trending);

        public abstract Builder videosError(Throwable videosError);

        public abstract Builder popular(Map<Integer, ItemCard> popular);

        public abstract Builder error(Throwable error);

        public abstract Builder loadingTrending(boolean loadingTrending);

        public abstract Builder itemLoadingCount(int itemLoadingCount);

        public abstract ViewStateHome build();
    }
}
