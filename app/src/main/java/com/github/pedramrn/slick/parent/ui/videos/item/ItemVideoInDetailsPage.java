package com.github.pedramrn.slick.parent.ui.videos.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowVideoInDetailsPageBinding;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 * Created on: 2018-07-14
 */
public class ItemVideoInDetailsPage extends ItemVideoAbs<RowVideoInDetailsPageBinding> implements RemovableOnError {

    public ItemVideoInDetailsPage(long id, Video video) {
        super(id, video);
    }

    @Override
    public int getLayout() {
        return R.layout.row_video_in_details_page;
    }

    @Override
    public void bind(RowVideoInDetailsPageBinding viewBinding, int position) {
        applicationContext = viewBinding.getRoot().getContext().getApplicationContext();
        viewBinding.layoutShimmer.stopShimmerAnimation();
        viewBinding.imageViewThumbnail.load(video.thumbnail());
        viewBinding.textViewTitle.setBackground(null);
        viewBinding.textViewTitle.setText(video.name());
    }

    @Override
    public boolean removableByTag(String tag) {
        return false;
    }
}
