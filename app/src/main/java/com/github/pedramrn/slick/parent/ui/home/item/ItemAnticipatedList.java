package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowAnticipatedListBinding;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemAnticipatedList extends Item<RowAnticipatedListBinding> {

    private final RecyclerView.Adapter adapter;
    private final SnapHelper snapHelper = new PagerSnapHelper();
    private final LinearLayoutManager layoutManager;


    public ItemAnticipatedList(Context context, RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        this.layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public int getLayout() {
        return R.layout.row_anticipated_list;
    }

    @Override
    public void bind(RowAnticipatedListBinding viewBinding, int position) {
        // TODO: 2017-06-22 add pager indicator
        RecyclerView recyclerView = viewBinding.recyclerViewAnticipated;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void unbind(ViewHolder<RowAnticipatedListBinding> holder) {
        holder.binding.recyclerViewAnticipated.setOnFlingListener(null);
        super.unbind(holder);
    }
}
