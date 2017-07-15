package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCastListBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemListHorizontal extends Item<RowCastListBinding> {


    private final RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private final String SCROLL_POS;
    private int scrollPos;

    public ItemListHorizontal(Context context, RecyclerView.Adapter adapter, String tag) {
        this.adapter = adapter;
        SCROLL_POS = "SCROLL_POS_" + tag;
        layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public int getLayout() {
        return R.layout.row_cast_list;
    }

    @Override
    public void bind(RowCastListBinding viewBinding, int position) {
        RecyclerView recyclerView = viewBinding.recyclerViewCasts;
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        layoutManager.scrollToPosition(scrollPos);
    }

    public void onSaveViewState(View view, Bundle outState) {
        outState.putInt(SCROLL_POS, layoutManager != null ? layoutManager.findFirstCompletelyVisibleItemPosition() : 0);
    }

    public void onRestoreViewState(View view, Bundle savedViewState) {
        scrollPos = savedViewState.getInt(SCROLL_POS);
    }
}
