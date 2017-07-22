package com.github.pedramrn.slick.parent.ui.videos.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowVideoBinding;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-22
 */

public class ItemVideo extends Item<RowVideoBinding> {

    private final Video video;

    public ItemVideo(long id, Video video) {
        super(id);
        this.video = video;
    }

    @Override
    public int getLayout() {
        return R.layout.row_video;
    }

    @Override
    public void bind(RowVideoBinding viewBinding, int position) {
        viewBinding.imageViewThumbnail.load(video.thumbnail());
        viewBinding.textViewName.setBackground(null);
        viewBinding.textViewChannelDateViews.setBackground(null);
        viewBinding.textViewName.setText(video.name());
        viewBinding.textViewChannelDateViews.setText(String.format("Type: %s", video.type()));
    }
}
