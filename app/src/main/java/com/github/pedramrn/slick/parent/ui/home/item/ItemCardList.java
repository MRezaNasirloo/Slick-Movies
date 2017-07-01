package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardListBinding;
import com.github.pedramrn.slick.parent.ui.custom.StartSnapHelper;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationSideMargin;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardList extends Item<RowCardListBinding> {

    private final RecyclerView.Adapter adapter;

    public ItemCardList(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_list;
    }

    @Override
    public void bind(RowCardListBinding viewBinding, int position) {
        // TODO: 2017-06-22 add pager indicator
        Context context = viewBinding.getRoot().getContext();
        RecyclerView recyclerView = viewBinding.recyclerViewCard;
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new ItemDecorationSideMargin(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin)));
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
