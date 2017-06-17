package com.github.pedramrn.slick.parent.ui.details.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBackdropBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-17
 */

public class ItemBackdropProgressive extends Item<RowBackdropBinding> {

    @Override
    public int getLayout() {
        return R.layout.row_backdrop;
    }

    @Override
    public void bind(RowBackdropBinding viewBinding, int position) {
    }

    public ItemBackdropProgressive(long id) {
        super(id);
    }

    @Override
    public long getId() {
        return super.getId();
    }
}
