package com.github.pedramrn.slick.parent.ui.home.state;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;
import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */
@AutoValue
public abstract class ViewStateHome {
    public abstract Map<Integer, Item> trending();

    public abstract Map<Integer, Item> popular();

    public abstract List<Item> upcoming();

    public abstract boolean loadingTrending();

    public abstract int itemLoadingCountTrending();

    public abstract int pageTrending();

    public abstract boolean loadingPopular();

    public abstract int itemLoadingCountPopular();

    public abstract int pagePopular();

    @Nullable
    public abstract Throwable errorUpcoming();

    @Nullable
    public abstract Throwable error();


    public abstract Builder toBuilder();

    public static ViewStateHome create(
            Map<Integer, Item> trending,
            Map<Integer, Item> popular,
            List<Item> upcoming,
            boolean loadingTrending,
            int itemLoadingCountTrending,
            int pageTrending,
            boolean loadingPopular,
            int itemLoadingCountPopular,
            int pagePopular,
            Throwable errorUpcoming,
            Throwable error
    ) {
        return builder()
                .trending(trending)
                .popular(popular)
                .upcoming(upcoming)
                .loadingTrending(loadingTrending)
                .itemLoadingCountTrending(itemLoadingCountTrending)
                .pageTrending(pageTrending)
                .loadingPopular(loadingPopular)
                .itemLoadingCountPopular(itemLoadingCountPopular)
                .pagePopular(pagePopular)
                .errorUpcoming(errorUpcoming)
                .error(error)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateHome.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder trending(Map<Integer, Item> trending);

        public abstract Builder loadingTrending(boolean loadingTrending);

        public abstract Builder itemLoadingCountTrending(int itemLoadingCountTrending);

        public abstract Builder loadingPopular(boolean loadingPopular);

        public abstract Builder itemLoadingCountPopular(int itemLoadingCountPopular);

        public abstract Builder error(Throwable error);

        public abstract Builder pageTrending(int pageTrending);

        public abstract Builder pagePopular(int pagePopular);

        public abstract Builder upcoming(List<Item> upcoming);

        public abstract Builder errorUpcoming(Throwable errorUpcoming);

        public abstract Builder popular(Map<Integer, Item> popular);

        public abstract ViewStateHome build();
    }
}
