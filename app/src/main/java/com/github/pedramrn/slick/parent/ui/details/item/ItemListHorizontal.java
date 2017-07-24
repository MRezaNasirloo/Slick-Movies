package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCastListBinding;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemListHorizontal extends Item<RowCastListBinding> {


    private final RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private final String SCROLL_POS;
    private int scrollPos;

    public ItemListHorizontal(Context context, GroupAdapter adapter, String tag, OnItemClickListener onItemClickListener) {
        this.adapter = adapter;
        SCROLL_POS = "SCROLL_POS_" + tag;
        adapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public int getLayout() {
        return R.layout.row_cast_list;
    }

    @Override
    public void bind(RowCastListBinding viewBinding, int position) {
        RecyclerView recyclerView = viewBinding.recyclerViewCasts;
        layoutManager = (LinearLayoutManager) layoutManager(viewBinding.getRoot());
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        layoutManager.scrollToPosition(scrollPos);
    }

    @NonNull
    protected RecyclerView.LayoutManager layoutManager(View root) {
        return new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    public void onSaveViewState(View view, Bundle outState) {
        outState.putInt(SCROLL_POS, layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0);
    }

    public void onRestoreViewState(View view, Bundle savedViewState) {
        scrollPos = savedViewState.getInt(SCROLL_POS);
    }
}
