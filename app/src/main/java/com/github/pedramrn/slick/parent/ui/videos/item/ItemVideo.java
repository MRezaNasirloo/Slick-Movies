package com.github.pedramrn.slick.parent.ui.videos.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowVideoBinding;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-07-22
 */

public class ItemVideo extends ItemVideoAbs<RowVideoBinding> {

    public ItemVideo(long id, Video video) {
        super(id, video);
    }

    @Override
    public int getLayout() {
        return R.layout.row_video;
    }

    @Override
    public void bind(RowVideoBinding viewBinding, int position) {
        applicationContext = viewBinding.getRoot().getContext().getApplicationContext();
        viewBinding.layoutShimmer.stopShimmerAnimation();
        viewBinding.imageViewThumbnail.load(video.thumbnail());
        viewBinding.textViewName.setBackground(null);
        viewBinding.textViewChannelDateViews.setBackground(null);
        viewBinding.textViewName.setText(video.name());
        viewBinding.textViewChannelDateViews.setText(String.format("Type: %s", video.type()));
    }

}
