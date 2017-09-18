package com.github.pedramrn.slick.parent.ui.home.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowLoadingBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-09
 */
public class ItemLoading extends Item<RowLoadingBinding> implements RemovableOnError {


    public ItemLoading(long id) {
        super(id);
    }

    @Override
    public int getLayout() {
        return R.layout.row_loading;
    }

    @Override
    public void bind(RowLoadingBinding viewBinding, int position) {
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
