package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBackdropListBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-17
 */

public class ItemBackdropList extends Item<RowBackdropListBinding> {


    private final RecyclerView.Adapter adapter;

    public ItemBackdropList(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getLayout() {
        return R.layout.row_backdrop_list;
    }

    @Override
    public void bind(RowBackdropListBinding viewBinding, int position) {
        RecyclerView recyclerView = viewBinding.recyclerViewBackdrop;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }
}
