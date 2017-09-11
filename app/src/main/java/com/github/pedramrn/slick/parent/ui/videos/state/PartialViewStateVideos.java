package com.github.pedramrn.slick.parent.ui.videos.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.github.pedramrn.slick.parent.ui.videos.item.ItemVideoProgressive;
import com.xwray.groupie.Item;

import java.util.List;

/**

 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-22
 */

public final class PartialViewStateVideos {
    private PartialViewStateVideos() {
        //no instance
    }

    public static class Videos implements PartialViewState<ViewStateVideos> {
        private final List<Item> videos;

        public Videos(List<Item> videos) {
            this.videos = videos;
        }

        @Override
        public ViewStateVideos reduce(ViewStateVideos state) {
            return state.toBuilder().videos(videos).build();
        }
    }

    public static class VideosProgressive extends PartialProgressive implements PartialViewState<ViewStateVideos> {

        public VideosProgressive(int count, String tag) {
            super(count, tag, new ProgressiveVideosRenderer());
        }


        @Override
        public ViewStateVideos reduce(ViewStateVideos state) {
            return state.toBuilder().videos(state.videos()).build();
        }

        private static class ProgressiveVideosRenderer implements ItemRenderer {

            @Override
            public Item render(long id, String tag) {
                return new ItemVideoProgressive(id, tag);
            }
        }
    }

    public static class VideosError implements PartialViewState<ViewStateVideos> {

        private final Throwable throwable;

        public VideosError(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateVideos reduce(ViewStateVideos state) {
            return state.toBuilder().errorVideos(throwable).build();
        }
    }

}
