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

        private final List<ItemVideo> progressive;

        public ProgressiveVideosImpl() {
            progressive = new ArrayList<>(3);
            progressive.add(new ItemVideoProgressiveImpl());
            progressive.add(new ItemVideoProgressiveImpl());
            progressive.add(new ItemVideoProgressiveImpl());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().items(progressive).build();
        }
    }


    class Trending implements ViewStateHomePartial {

        private final List<ItemCard> movies;

        public Trending(List<ItemCard> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().trending(movies).build();
        }
    }

    abstract class CardProgressive implements ViewStateHomePartial {

        protected final List<ItemCard> progressive;


        public CardProgressive(int count, String tag) {
            // TODO: 2017-07-01 optimize for other screen sizes
            progressive = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                progressive.add(new ItemCardProgressiveImpl(IdBank.nextId(tag)));
            }
        }

        public CardProgressive(String tag) {
            progressive = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                progressive.add(new ItemCardProgressiveImpl(IdBank.nextId(tag)));
            }
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
            return viewStateHome.toBuilder().trending(progressive).build();
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
            return viewStateHome.toBuilder().popular(progressive).build();
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

        public Popular(List<ItemCard> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().popular(movies).build();
        }
    }
}
