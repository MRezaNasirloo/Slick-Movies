package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowOverviewBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-17
 */

public class ItemOverview extends Item<RowOverviewBinding> implements OnItemAction {

    private final String overview;

    public ItemOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int getLayout() {
        return R.layout.row_overview;
    }

    @Override
    public void bind(RowOverviewBinding viewBinding, int position) {
        viewBinding.textViewOverview.setText(overview);
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        //no-op
    }
}
