package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBackdropBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-17
 */

public class ItemBackdropProgressive extends Item<RowBackdropBinding> implements OnItemAction, RemovableOnError {

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

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        //no-op
    }

    @Override
    public boolean removableByTag(String tag) {
        return true;
    }
}
