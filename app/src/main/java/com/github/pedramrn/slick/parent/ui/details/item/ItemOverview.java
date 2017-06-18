package com.github.pedramrn.slick.parent.ui.details.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowOverviewBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-17
 */

public class ItemOverview extends Item<RowOverviewBinding> {

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
}