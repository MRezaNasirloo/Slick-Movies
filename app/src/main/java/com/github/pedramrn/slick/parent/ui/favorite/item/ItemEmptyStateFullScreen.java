package com.github.pedramrn.slick.parent.ui.favorite.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowEmptyStateFullScreenBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-25
 */

public class ItemEmptyStateFullScreen extends Item<RowEmptyStateFullScreenBinding> {
    @Override
    public int getLayout() {
        return R.layout.row_empty_state_full_screen;
    }

    @Override
    public void bind(RowEmptyStateFullScreenBinding viewBinding, int position) {
        viewBinding.imageView.load(R.drawable.empty_state_cat);
    }
}
