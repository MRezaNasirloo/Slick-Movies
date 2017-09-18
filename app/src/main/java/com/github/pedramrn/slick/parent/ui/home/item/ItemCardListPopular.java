package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.RecycledViewPool;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardListPopularBinding;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListAbs;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListPopular;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-18
 */
public class ItemCardListPopular extends ItemCardListBase<RowCardListPopularBinding, RecyclerViewCardListPopular> {
    public ItemCardListPopular(@NonNull Context context, @NonNull String tag, RecycledViewPool viewPool) {
        super(context, tag, viewPool);
    }

    @Override
    protected RecyclerViewCardListAbs getRecyclerViewCardListAbs(RowCardListPopularBinding binding) {
        return binding.recyclerViewPopular;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_list_popular;
    }
}
