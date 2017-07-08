package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */
@AutoValue
abstract class ViewStateHome {
    @Nullable
    public abstract List<ItemVideo> anticipated();

    public abstract List<Item> trending();

    public abstract List<Item> popular();

    public abstract boolean loadingTrending();

    public abstract int itemLoadingCountTrending();

    public abstract boolean loadingPopular();

    public abstract int itemLoadingCountPopular();

    @Nullable
    public abstract Throwable videosError();

    @Nullable
    public abstract Throwable error();


    public abstract Builder toBuilder();

    public static ViewStateHome create(List<ItemVideo> anticipated, List<Item> trending, List<Item> popular, boolean loadingTrending,
                                       int itemLoadingCountTrending, boolean loadingPopular, int itemLoadingCountPopular, Throwable videosError,
                                       Throwable error) {
        return builder()
                .anticipated(anticipated)
                .trending(trending)
                .popular(popular)
                .loadingTrending(loadingTrending)
                .itemLoadingCountTrending(itemLoadingCountTrending)
                .loadingPopular(loadingPopular)
                .itemLoadingCountPopular(itemLoadingCountPopular)
                .videosError(videosError)
                .error(error)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateHome.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder anticipated(List<ItemVideo> anticipated);

        public abstract Builder trending(List<Item> trending);

        public abstract Builder popular(List<Item> popular);

        public abstract Builder loadingTrending(boolean loadingTrending);

        public abstract Builder itemLoadingCountTrending(int itemLoadingCountTrending);

        public abstract Builder loadingPopular(boolean loadingPopular);

        public abstract Builder itemLoadingCountPopular(int itemLoadingCountPopular);

        public abstract Builder videosError(Throwable videosError);

        public abstract Builder error(Throwable error);

        public abstract ViewStateHome build();
    }
}
