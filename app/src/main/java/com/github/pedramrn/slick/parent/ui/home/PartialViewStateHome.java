package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.item.ItemBannerProgressive;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.item.ItemProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public final class PartialViewStateHome {

    private PartialViewStateHome() {
        //no instance
    }

    static class Upcoming implements PartialViewState<ViewStateHome> {
        private final List<Item> movies;

        public Upcoming(List<Item> movies) {

            this.movies = movies;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome state) {
            return state.toBuilder().upcoming(movies).build();
        }
    }

    static class UpcomingError extends Error {

        public UpcomingError(Throwable throwable) {
            super(throwable);
        }

        @Override
        public ViewStateHome reduce(ViewStateHome state) {
            return state.toBuilder().errorUpcoming(throwable).build();
        }
    }

    /*static class VideosImpl implements PartialViewState<ViewStateHome> {

        private final List<Item> itemVideos;

        public VideosImpl(List<Item> itemVideos) {
            this.itemVideos = itemVideos;
        }

        public List<Item> getItems() {
            return itemVideos;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().anticipated(itemVideos).build();
        }
    }

    static class VideosErrorImpl implements PartialViewState<ViewStateHome> {
        private final Throwable e;

        public VideosErrorImpl(Throwable e) {
            this.e = e;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().errorVideos(e).build();
        }
    }*/

    static class ProgressiveBannerImpl extends ItemProgressive implements PartialViewState<ViewStateHome> {


        public ProgressiveBannerImpl(int count, String tag) {
            super(count, tag, new ItemRendererBanner());
        }

        public ProgressiveBannerImpl(String tag) {
            super(tag, new ItemRendererBanner());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().upcoming(progressive).build();
        }

        static class ItemRendererBanner implements ItemRenderer {
            @Override
            public Item render(long id, String tag) {
                return new ItemBannerProgressive(id, tag);
            }
        }
    }


    static class Trending implements PartialViewState<ViewStateHome> {

        private final List<Item> movies;
        private final boolean loading;

        public Trending(List<Item> movies, boolean loading) {
            this.movies = movies;
            this.loading = loading;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            /*List<Item> trending = viewStateHome.trending();
            if (trending != null) {
                trending.addAll(movies);
            } else {
                trending = movies;
            }*/
            return viewStateHome.toBuilder()
                    .trending(new ArrayList<>(movies))
                    .loadingTrending(loading)
                    .itemLoadingCountTrending(movies.size())
                    .pageTrending(viewStateHome.pageTrending() + 1)
                    .build();
        }
    }

    static class ItemRendererProgressiveCard implements ItemRenderer {

        @Override
        public Item render(long id, String tag) {
            return ItemCardProgressiveImpl.create(id).render(tag);
        }
    }

    static class CardProgressiveTrending extends ItemProgressive implements PartialViewState<ViewStateHome> {

        public CardProgressiveTrending(int count, String tag) {
            super(count, tag, new ItemRendererProgressiveCard());
        }

        public CardProgressiveTrending(String tag) {
            super(tag, new ItemRendererProgressiveCard());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().trending(reduce(viewStateHome.trending())).build();
        }
    }

    static class CardProgressivePopular extends ItemProgressive implements PartialViewState<ViewStateHome> {

        public CardProgressivePopular(int count, String tag) {
            super(count, tag, new ItemRendererProgressiveCard());
        }

        public CardProgressivePopular(String tag) {
            super(tag, new ItemRendererProgressiveCard());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().popular(reduce(viewStateHome.popular())).build();
        }
    }

    static class Error implements PartialViewState<ViewStateHome> {

        protected final Throwable throwable;

        public Error(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().error(throwable).build();
        }
    }

    static class Popular implements PartialViewState<ViewStateHome> {
        private final List<Item> movies;
        private final boolean isLoading;

        public Popular(List<Item> movies, boolean isLoading) {
            this.movies = movies;
            this.isLoading = isLoading;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder()
                    .popular(new ArrayList<>(movies))
                    .loadingPopular(isLoading)
                    .itemLoadingCountPopular(movies.size())
                    .pagePopular(viewStateHome.pagePopular() + 1)
                    .build();
        }
    }
}
