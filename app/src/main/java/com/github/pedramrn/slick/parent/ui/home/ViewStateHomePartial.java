package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideoProgressiveImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public interface ViewStateHomePartial {

    ViewStateHome reduce(ViewStateHome viewStateHome);

    class VideosImpl implements ViewStateHomePartial {

        private final List<ItemVideo> itemVideos;

        public VideosImpl(List<ItemVideo> itemVideos) {
            this.itemVideos = itemVideos;
        }

        public List<ItemVideo> getItemVideos() {
            return itemVideos;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().items(itemVideos).build();
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

        private List<ItemVideo> progressive;

        public ProgressiveVideosImpl() {
            progressive = new ArrayList<>(3);
            progressive.add(0, new ItemVideoProgressiveImpl());
            progressive.add(1, new ItemVideoProgressiveImpl());
            progressive.add(2, new ItemVideoProgressiveImpl());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().items(progressive).build();
        }
    }


    class Trending implements ViewStateHomePartial {

        private final List<ItemCard> movies;
        private final boolean loading;

        public Trending(List<ItemCard> movies, boolean loading) {
            this.movies = movies;
            this.loading = loading;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            /*List<ItemCard> trending = viewStateHome.trending();
            if (trending != null) {
                trending.addAll(movies);
            } else {
                trending = movies;
            }*/
            return viewStateHome.toBuilder()
                    .trending(new ArrayList<>(movies))
                    .loadingTrending(loading)
                    .itemLoadingCountTrending(movies.size())
                    .build();
        }
    }

    abstract class CardProgressive implements ViewStateHomePartial {

        protected final List<ItemCard> progressive;


        public CardProgressive(int count, String tag) {
            progressive = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                int id = IdBank.nextId(tag);
                progressive.add(ItemCardProgressiveImpl.create(id));
            }
        }

        public CardProgressive(String tag) {
            progressive = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                int id = IdBank.nextId(tag);
                progressive.add(ItemCardProgressiveImpl.create(id));
            }
        }

        protected List<ItemCard> reduce(List<ItemCard> items) {
            if (items != null) {
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
        private final List<ItemCard> movies;
        private final boolean isLoading;

        public Popular(List<ItemCard> movies, boolean isLoading) {
            this.movies = movies;
            this.isLoading = isLoading;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder()
                    .popular(new ArrayList<>(movies))
                    .loadingPopular(isLoading)
                    .itemLoadingCountPopular(movies.size())
                    .build();
        }
    }
}
