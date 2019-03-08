package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.TextView;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCastHorizontalBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.databinding.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemCastProgressive extends BindableItem<RowCastHorizontalBinding> implements OnItemAction, RemovableOnError {

    public ItemCastProgressive(long id) {
        super(id);
    }

    @Override
    public int getLayout() {
        return R.layout.row_cast_horizontal;
    }

    @Override
    public void bind(@NonNull RowCastHorizontalBinding viewBinding, int position) {
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
    public void unbind(@NonNull ViewHolder<RowCastHorizontalBinding> holder) {
        holder.binding.textViewCharacter.setBackground(null);
        holder.binding.textViewName.setBackground(null);
        super.unbind(holder);
    }

    @Override
    public int getSpanSize(int spanCount, int position) {
        return 3;
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
