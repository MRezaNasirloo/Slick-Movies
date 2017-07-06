package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideoProgressiveImpl;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public interface ViewStateHomePartial {

    ViewStateHome reduce(ViewStateHome viewStateHome);

    class VideosImpl implements ViewStateHomePartial {

        private final Map<Integer, ItemVideo> itemVideos;

        public VideosImpl(Map<Integer, ItemVideo> itemVideos) {
            this.itemVideos = itemVideos;
        }

        public Map<Integer, ItemVideo> getItemVideos() {
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

        private Map<Integer, ItemVideo> progressive;

        public ProgressiveVideosImpl() {
            progressive = new LinkedHashMap<>(3);
            progressive.put(0, new ItemVideoProgressiveImpl());
            progressive.put(1, new ItemVideoProgressiveImpl());
            progressive.put(2, new ItemVideoProgressiveImpl());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().items(progressive).build();
        }
    }


    class Trending implements ViewStateHomePartial {

        private final Map<Integer, ItemCard> movies;
        private final boolean loading;

        public Trending(Map<Integer, ItemCard> movies, boolean loading) {
            this.movies = movies;
            this.loading = loading;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            Map<Integer, ItemCard> trending = viewStateHome.trending();
            if (trending != null) {
                trending.putAll(movies);
            } else {
                trending = movies;
            }
            return viewStateHome.toBuilder()
                    .trending(trending)
                    .loadingTrending(loading)
                    .itemLoadingCount(movies.size())
                    .build();
        }
    }

    abstract class CardProgressive implements ViewStateHomePartial {

        protected final Map<Integer, ItemCard> progressive;


        public CardProgressive(int count, String tag) {
            progressive = new LinkedHashMap<>(count);
            for (int i = 0; i < count; i++) {
                int id = IdBank.nextId(tag);
                progressive.put(id, ItemCardProgressiveImpl.create(id));
            }
        }

        public CardProgressive(String tag) {
            progressive = new LinkedHashMap<>(3);
            for (int i = 0; i < 3; i++) {
                int id = IdBank.nextId(tag);
                progressive.put(id, ItemCardProgressiveImpl.create(id));
            }
        }

        protected Map<Integer, ItemCard> reduce(Map<Integer, ItemCard> items) {
            if (items != null) {
                items.putAll(progressive);
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
            return viewStateHome.toBuilder().trending(reduce(viewStateHome.popular())).build();
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
        private final Map<Integer, ItemCard> movies;

        public Popular(Map<Integer, ItemCard> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().popular(movies).build();
        }
    }
}
