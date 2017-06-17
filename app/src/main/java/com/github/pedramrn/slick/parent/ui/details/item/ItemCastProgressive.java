package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCastBinding;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemCastProgressive extends Item<RowCastBinding> {
    @Override
    public int getLayout() {
        return R.layout.row_cast;
    }

    @Override
    public void bind(RowCastBinding viewBinding, int position) {
        Context context = viewBinding.imageViewProfile.getContext();
        viewBinding.imageViewProfile.setImageResource(R.drawable.circle);
        viewBinding.textViewName.setBackgroundResource(R.drawable.line);
        viewBinding.textViewCharacter.setBackgroundResource(R.drawable.line);
        viewBinding.textViewName.setText("                         ");
        viewBinding.textViewCharacter.setText("                      ");
        setBackgroundTint(context, viewBinding.textViewName, R.color.color_gray_1);
        setBackgroundTint(context, viewBinding.textViewCharacter, R.color.color_gray_2);
    }

    private void setBackgroundTint(Context context, TextView view, @ColorRes int color) {
        view.getBackground().setColorFilter(ResourcesCompat.getColor(context.getResources(), color, null), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void unbind(ViewHolder<RowCastBinding> holder) {
        holder.binding.textViewCharacter.setBackground(null);
        holder.binding.textViewName.setBackground(null);
        super.unbind(holder);
    }
}
