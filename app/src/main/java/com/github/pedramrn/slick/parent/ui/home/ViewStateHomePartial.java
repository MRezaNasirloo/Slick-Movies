package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideoProgressiveImpl;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public interface ViewStateHomePartial {

    ViewStateHome reduce(ViewStateHome viewStateHome);

    class VideosImpl implements ViewStateHomePartial {

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

    class VideosErrorImpl implements ViewStateHomePartial {
        private final Throwable e;

        public VideosErrorImpl(Throwable e) {
            this.e = e;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().videosError(e).build();
        }
    }

    class ProgressiveVideosImpl implements ViewStateHomePartial {

        private List<Item> progressive;

        public ProgressiveVideosImpl() {
            progressive = new ArrayList<>(3);
            progressive.add(0, new ItemVideoProgressiveImpl().render(0));
            progressive.add(1, new ItemVideoProgressiveImpl().render(1));
            progressive.add(2, new ItemVideoProgressiveImpl().render(2));
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().anticipated(progressive).build();
        }
    }


    class Trending implements ViewStateHomePartial {

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

    abstract class CardProgressive implements ViewStateHomePartial {

        protected final List<Item> progressive;


        public CardProgressive(int count, String tag) {
            progressive = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                int id = IdBank.nextId(tag);
                progressive.add(ItemCardProgressiveImpl.create(id).render(null));
            }
        }

        public CardProgressive(String tag) {
            progressive = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                int id = IdBank.nextId(tag);
                progressive.add(ItemCardProgressiveImpl.create(id).render(null));
            }
        }

        protected List<Item> reduce(List<Item> items) {
            if (items != null && items.size() > 0) {
                items.addAll(progressive);
            } else {
                items = progressive;
            }
            return items;
        }
    }

    class CardProgressiveTrending extends CardProgressive {

        public CardProgressiveTrending(int count, String tag) {
            super(count, tag);
        }

        public CardProgressiveTrending(String tag) {
            super(tag);
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().trending(reduce(viewStateHome.trending())).build();
        }
    }

    class CardProgressivePopular extends CardProgressive {

        public CardProgressivePopular(int count, String tag) {
            super(count, tag);
        }

        public CardProgressivePopular(String tag) {
            super(tag);
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().popular(reduce(viewStateHome.popular())).build();
        }
    }

    class Error implements ViewStateHomePartial {

        private final Throwable throwable;

        public Error(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().error(throwable).build();
        }
    }

    class Popular implements ViewStateHomePartial {
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
