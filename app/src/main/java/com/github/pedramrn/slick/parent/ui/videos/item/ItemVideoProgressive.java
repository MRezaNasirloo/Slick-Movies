package com.github.pedramrn.slick.parent.ui.videos.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowVideoBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-22
 */

public class ItemVideoProgressive extends ItemVideo implements RemovableOnError {
    public ItemVideoProgressive(long id, String tag) {
        super(id, null);
    }

    @Override
    public void bind(RowVideoBinding viewBinding, int position) {
        viewBinding.layoutShimmer.startShimmerAnimation();
        viewBinding.textViewName.setText("      ");
        viewBinding.textViewName.setBackgroundResource(R.drawable.line);
        viewBinding.textViewChannelDateViews.setText("                   ");
        viewBinding.textViewChannelDateViews.setBackgroundResource(R.drawable.line);
        viewBinding.imageViewThumbnail.setImageResource(R.drawable.rectangle_no_corners);
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        //no-op
    }

    @Override
    public boolean removableByTag(String tag) {
        return true;
    }
}
