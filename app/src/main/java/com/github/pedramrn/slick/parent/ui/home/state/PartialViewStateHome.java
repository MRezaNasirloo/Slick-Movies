package com.github.pedramrn.slick.parent.ui.home.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.item.ItemBannerProgressive;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovieProgressive;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.home.item.ItemError;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public final class PartialViewStateHome {

    private PartialViewStateHome() {
        //no instance
    }

    public static class Upcoming implements PartialViewState<ViewStateHome> {
        private final List<Item> movies;

        public Upcoming(List<Item> movies) {

            this.movies = movies;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome state) {
            return state.toBuilder().upcoming(movies).build();
        }
    }

    public static class UpcomingErrorTrending extends ErrorTrending {

        public UpcomingErrorTrending(Throwable throwable) {
            super(throwable);
        }

        @Override
        public ViewStateHome reduce(ViewStateHome state) {
            return state.toBuilder().errorUpcoming(throwable).build();
        }
    }

    /*public static class VideosImpl implements PartialViewState<ViewStateHome> {

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

    public static class VideosErrorImpl implements PartialViewState<ViewStateHome> {
        private final Throwable e;

        public VideosErrorImpl(Throwable e) {
            this.e = e;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().errorVideos(e).build();
        }
    }*/

    public static class ProgressiveBannerImpl extends PartialProgressive implements PartialViewState<ViewStateHome> {


        public ProgressiveBannerImpl(int count, String tag) {
            super(count, tag, new ItemRendererBanner());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().upcoming(reduce(viewStateHome.upcoming())).build();
        }

        public static class ItemRendererBanner implements ItemRenderer {
            @Override
            public Item render(long id, String tag) {
                return new ItemBannerProgressive(id, tag);
            }
        }
    }


    public static class Trending implements PartialViewState<ViewStateHome> {

        private final Map<Integer, Item> movies;

        public Trending(Map<Integer, Item> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            Map<Integer, Item> trending = viewStateHome.trending();
            Iterator<Item> iterator = trending.values().iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item instanceof ItemError || item instanceof ItemCardMovieProgressive) {
                    iterator.remove();
                }

            }
            trending.putAll(movies);
            return viewStateHome.toBuilder()
                    .trending(new LinkedHashMap<>(trending))
                    .itemLoadingCountTrending(trending.size())
                    .error(null)
                    .build();
        }
    }

    public static class TrendingLoaded implements PartialViewState<ViewStateHome> {

        private final boolean loading;

        public TrendingLoaded(boolean loaded) {
            this.loading = !loaded;

        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            Map<Integer, Item> trending = viewStateHome.trending();
            Iterator<Item> iterator = trending.values().iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item instanceof ItemError || item instanceof ItemCardMovieProgressive) {
                    iterator.remove();
                }

            }
            return viewStateHome.toBuilder()
                    .trending(new LinkedHashMap<>(trending))
                    .loadingTrending(loading)
                    .itemLoadingCountTrending(trending.size())
                    .pageTrending(viewStateHome.pageTrending() + 1)
                    .error(null)
                    .build();
        }
    }


    public static class ItemRendererProgressiveCard implements ItemRenderer {

        @Override
        public Item render(long id, String tag) {
            return ItemCardProgressiveImpl.create(id).render(tag);
        }
    }

    public static class CardProgressiveTrending extends PartialProgressive implements PartialViewState<ViewStateHome> {

        public CardProgressiveTrending(int count, String tag) {
            super(count, tag, new ItemRendererProgressiveCard());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().trending(reduce(viewStateHome.trending())).loadingTrending(true).build();
        }
    }

    public static class CardProgressivePopular extends PartialProgressive implements PartialViewState<ViewStateHome> {

        public CardProgressivePopular(int count, String tag) {
            super(count, tag, new ItemRendererProgressiveCard());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().popular(reduce(viewStateHome.popular())).build();
        }
    }

    public static class ErrorTrending implements PartialViewState<ViewStateHome> {

        protected final Throwable throwable;

        public ErrorTrending(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            Item itemError = null;
            Map<Integer, Item> trending = viewStateHome.trending();
            //because there are 6 progressive items at most
            /*int limit = size - 6 < 0 ? 0 : size - 6;
            for (int i = size - 1; i >= limit; i--)*/
            Iterator<Item> iterator = trending.values().iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item instanceof ItemCardMovieProgressive) {
                    iterator.remove();
                }
                else if (item instanceof ItemError) {
                    itemError = item;
                    iterator.remove();
                }

            }

            if (itemError == null) {
                itemError = new ItemError(-1, throwable.getMessage());
            }
            trending.put(((int) itemError.getId()), itemError);
            return viewStateHome.toBuilder()
                    .error(throwable)
                    .loadingTrending(true)
                    .trending(new LinkedHashMap<>(trending))
                    .build();
        }
    }

    public static class Popular implements PartialViewState<ViewStateHome> {
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
