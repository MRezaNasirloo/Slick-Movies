package com.github.pedramrn.slick.parent.ui.details.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBackdropEmptyBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-15
 */

public class ItemBackdropEmpty extends Item<RowBackdropEmptyBinding> {

    public ItemBackdropEmpty() {
        super(0);
    }

    @Override
    public int getLayout() {
        return R.layout.row_backdrop_empty;
    }

    @Override
    public void bind(RowBackdropEmptyBinding viewBinding, int position) {
        viewBinding.imageViewBackdrop.load(R.drawable.empty_state_cat);
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    private static final String TAG = ItemBackdropEmpty.class.getSimpleName();
}
