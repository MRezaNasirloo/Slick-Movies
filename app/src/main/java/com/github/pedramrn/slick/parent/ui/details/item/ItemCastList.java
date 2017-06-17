package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCastListBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemCastList extends Item<RowCastListBinding> {


    private final RecyclerView.Adapter adapter;

    public ItemCastList(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getLayout() {
        return R.layout.row_cast_list;
    }

    @Override
    public void bind(RowCastListBinding viewBinding, int position) {
        RecyclerView recyclerView = viewBinding.recyclerViewCasts;
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }
}
