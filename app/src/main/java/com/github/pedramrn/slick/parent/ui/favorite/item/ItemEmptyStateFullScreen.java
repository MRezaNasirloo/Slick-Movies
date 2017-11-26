package com.github.pedramrn.slick.parent.ui.favorite.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowEmptyStateFullScreenBinding;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-25
 */

public class ItemEmptyStateFullScreen extends Item<RowEmptyStateFullScreenBinding> implements RemovableOnError {
    @Override
    public int getLayout() {
        return R.layout.row_empty_state_full_screen;
    }

    @Override
    public void bind(RowEmptyStateFullScreenBinding viewBinding, int position) {
        viewBinding.imageView.load(R.drawable.empty_state_cat);
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public boolean removableByTag(String tag) {
        return true;
    }
}
