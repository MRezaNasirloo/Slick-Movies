package com.github.pedramrn.slick.parent.ui.videos.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowVideoBinding;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-22
 */

public class ItemVideoProgressive extends ItemVideo {
    public ItemVideoProgressive(long id, String tag) {
        super(id, null);
    }

    @Override
    public void bind(RowVideoBinding viewBinding, int position) {
        viewBinding.textViewName.setText("      ");
        viewBinding.textViewName.setBackgroundResource(R.drawable.line);
        viewBinding.textViewChannelDateViews.setText("                   ");
        viewBinding.textViewChannelDateViews.setBackgroundResource(R.drawable.line);
        viewBinding.imageViewThumbnail.setImageResource(R.drawable.rectangle_no_corners);
    }

}
