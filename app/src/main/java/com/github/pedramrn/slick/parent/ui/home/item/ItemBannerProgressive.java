package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBannerBinding;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemBannerProgressive extends ItemBanner {

    public ItemBannerProgressive(long id, String tag) {
        super(id, null);
    }

    @Override
    public void bind(RowBannerBinding viewBinding, int position) {
        Context context = viewBinding.getRoot().getContext();
        viewBinding.textViewTitleAnticipated.setText("                         ");
        viewBinding.textViewTitleAnticipated.setBackgroundResource(R.drawable.line);
        viewBinding.imageViewPlay.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        viewBinding.imageViewThumbnail.setBackgroundResource(R.drawable.rectangle_no_corners);
        setBackgroundTint(context, viewBinding.textViewTitleAnticipated, R.color.color_gray_1);
    }

    private void setBackgroundTint(Context context, TextView view, @ColorRes int color) {
        view.getBackground().setColorFilter(ResourcesCompat.getColor(context.getResources(), color, null), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean removable() {
        return true;
    }
}
