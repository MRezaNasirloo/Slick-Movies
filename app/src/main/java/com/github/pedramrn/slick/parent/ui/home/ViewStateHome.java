package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */
@AutoValue
abstract class ViewStateHome {
    public abstract List<Item> anticipated();

    public abstract List<Item> trending();

    public abstract List<Item> popular();

    public abstract boolean loadingTrending();

    public abstract int itemLoadingCountTrending();

    public abstract int pageTrending();

    public abstract boolean loadingPopular();

    public abstract int itemLoadingCountPopular();

    public abstract int pagePopular();

    @Nullable
    public abstract Throwable videosError();

    @Nullable
    public abstract Throwable error();


    public abstract Builder toBuilder();

    public static ViewStateHome create(List<Item> anticipated, List<Item> trending, List<Item> popular, boolean loadingTrending,
                                       int itemLoadingCountTrending, int pageTrending, boolean loadingPopular, int itemLoadingCountPopular,
                                       int pagePopular, Throwable videosError, Throwable error) {
        return builder()
                .anticipated(anticipated)
                .trending(trending)
                .popular(popular)
                .loadingTrending(loadingTrending)
                .itemLoadingCountTrending(itemLoadingCountTrending)
                .pageTrending(pageTrending)
                .loadingPopular(loadingPopular)
                .itemLoadingCountPopular(itemLoadingCountPopular)
                .pagePopular(pagePopular)
                .videosError(videosError)
                .error(error)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateHome.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder anticipated(List<Item> anticipated);

        public abstract Builder trending(List<Item> trending);

        public abstract Builder popular(List<Item> popular);

        public abstract Builder loadingTrending(boolean loadingTrending);

        public abstract Builder itemLoadingCountTrending(int itemLoadingCountTrending);

        public abstract Builder loadingPopular(boolean loadingPopular);

        public abstract Builder itemLoadingCountPopular(int itemLoadingCountPopular);

        public abstract Builder videosError(Throwable videosError);

        public abstract Builder error(Throwable error);

        public abstract Builder pageTrending(int pageTrending);

        public abstract Builder pagePopular(int pagePopular);

        public abstract ViewStateHome build();
    }
}
