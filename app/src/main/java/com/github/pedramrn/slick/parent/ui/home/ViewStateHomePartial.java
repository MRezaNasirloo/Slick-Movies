package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.ui.details.model.Movie;

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

        public List<ItemVideo> getProgressive() {
            return progressive;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().items(progressive).build();
        }
    }

    class ProgressivePopular implements ViewStateHomePartial {

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().movies(new ArrayList<Movie>()).build();
        }
    }

}
