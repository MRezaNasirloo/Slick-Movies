package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowAnticipatedListBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemAnticipatedList extends Item<RowAnticipatedListBinding> {

    private final RecyclerView.Adapter adapter;

    public ItemAnticipatedList(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getLayout() {
        return R.layout.row_anticipated_list;
    }

    @Override
    public void bind(RowAnticipatedListBinding viewBinding, int position) {
        Context context = viewBinding.getRoot().getContext();
        RecyclerView recyclerView = viewBinding.recyclerViewAnticipated;
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
