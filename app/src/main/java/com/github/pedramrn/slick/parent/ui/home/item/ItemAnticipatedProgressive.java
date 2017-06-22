package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowAnticipatedBinding;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemAnticipatedProgressive extends ItemAnticipated {

    public ItemAnticipatedProgressive(long id) {
        super(id);
    }

    @Override
    public int getLayout() {
        return R.layout.row_anticipated;
    }

    @Override
    public void bind(RowAnticipatedBinding viewBinding, int position) {
        Context context = viewBinding.getRoot().getContext();
        viewBinding.textViewTitleAnticipated.setBackgroundResource(R.drawable.line);
        viewBinding.textViewTitleAnticipated.setText("                         ");
        setBackgroundTint(context, viewBinding.textViewTitleAnticipated, R.color.color_gray_1);
    }

    private void setBackgroundTint(Context context, TextView view, @ColorRes int color) {
        view.getBackground().setColorFilter(ResourcesCompat.getColor(context.getResources(), color, null), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void unbind(ViewHolder<RowAnticipatedBinding> holder) {
        holder.binding.textViewTitleAnticipated.setBackgroundColor(Color.TRANSPARENT);
        holder.binding.textViewTitleAnticipated.setBackground(null);
        super.unbind(holder);
    }
}
