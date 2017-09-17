package com.github.pedramrn.slick.parent.ui.home.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.PresenterHome;
import com.github.pedramrn.slick.parent.ui.home.item.ItemBannerError;
import com.github.pedramrn.slick.parent.ui.home.item.ItemBannerProgressive;
import com.github.pedramrn.slick.parent.ui.home.item.ItemError;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

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

    public static class UpcomingError implements PartialViewState<ViewStateHome> {

        private final Throwable throwable;

        public UpcomingError(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome state) {
            List<Item> upcoming = state.upcoming();
            Iterator<Item> iterator = upcoming.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (((RemovableOnError) item).removable()) {
                    iterator.remove();
                }
            }

            Item itemError = new ItemBannerError(-1, PresenterHome.BANNER, throwable);
            upcoming.add(itemError);

            return state.toBuilder()
                    .upcoming(new ArrayList<>(upcoming))
                    .errorUpcoming(throwable)
                    .build();
        }
    }

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


    public static class TrendingLoaded implements PartialViewState<ViewStateHome> {

        private final boolean loading;

        public TrendingLoaded(boolean loaded) {
            this.loading = !loaded;

        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            Map<Integer, Item> trending = viewStateHome.trending();
            removeRemovables(trending.values().iterator());
            return viewStateHome.toBuilder()
                    .trending(new TreeMap<>(trending))
                    .loadingTrending(loading)
                    .itemLoadingCountTrending(trending.size())
                    .pageTrending(viewStateHome.pageTrending() + 1)
                    .error(null)
                    .build();
        }
    }

    public static class ErrorPopular implements PartialViewState<ViewStateHome> {

        protected final Throwable throwable;

        public ErrorPopular(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            Map<Integer, Item> popular = viewStateHome.popular();
            removeRemovables(popular.values().iterator());

            Item itemError = new ItemError(-1, PresenterHome.POPULAR, throwable.getMessage());
            popular.put(((int) itemError.getId()), itemError);

            return viewStateHome.toBuilder()
                    .error(throwable)
                    .loadingPopular(true)
                    .popular(new TreeMap<>(popular))
                    .build();
        }
    }

    public static class Popular implements PartialViewState<ViewStateHome> {
        private final Map<Integer, Item> movies;

        public Popular(Map<Integer, Item> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            Map<Integer, Item> popular = viewStateHome.popular();
            removeRemovables(popular.values().iterator());
            popular.putAll(movies);
            return viewStateHome.toBuilder()
                    .popular(new TreeMap<>(popular))
                    .itemLoadingCountPopular(popular.size())
                    .error(null)
                    .build();
        }
    }
}
