package com.github.pedramrn.slick.parent.ui.details.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowSpaceBinding;
import com.xwray.groupie.databinding.BindableItem;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-27
 */

public class ItemSpace extends BindableItem<RowSpaceBinding> {
    @Override
    public int getLayout() {
        return R.layout.row_space;
    }

    @Override
    public void bind(RowSpaceBinding viewBinding, int position) {

    }

    @Override
    public boolean isClickable() {
        return false;
    }
}
