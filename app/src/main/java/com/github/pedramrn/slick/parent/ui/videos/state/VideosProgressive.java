package com.github.pedramrn.slick.parent.ui.videos.state;

import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.github.pedramrn.slick.parent.ui.videos.item.ItemVideoProgressive;
import com.github.pedramrn.slick.parent.util.Utils;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-06
 */
public class VideosProgressive extends PartialProgressive implements PartialViewState<ViewStateVideos> {

    public VideosProgressive(int count, String tag) {
        super(count, tag, new ProgressiveVideosRenderer());
    }


    @Override
    public ViewStateVideos reduce(ViewStateVideos state) {
        Utils.removeRemovables(state.videos().iterator(), "Videos");
        return state.toBuilder().videos(reduce(state.videos())).build();
    }

    private static class ProgressiveVideosRenderer implements ItemRenderer {

        @Override
        public Item render(long id, String tag) {
            return new ItemVideoProgressive(id, tag);
        }
    }
}
