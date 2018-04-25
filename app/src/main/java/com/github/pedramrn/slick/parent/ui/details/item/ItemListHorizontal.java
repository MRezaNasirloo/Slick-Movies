package com.github.pedramrn.slick.parent.ui.details.item;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCastListBinding;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemListHorizontal extends Item<RowCastListBinding> {


    private GroupAdapter adapter;
    private LinearLayoutManager layoutManager;
    private final String SCROLL_POS;
    private int scrollPos;

    public ItemListHorizontal(
            GroupAdapter adapter,
            String tag
    ) {
        this.adapter = adapter;
        SCROLL_POS = "SCROLL_POS_" + tag;
    }

    @Override
    public int getLayout() {
        return R.layout.row_cast_list;
    }

    @Override
    public void bind(RowCastListBinding viewBinding, int position) {
        long before = System.currentTimeMillis();
        RecyclerView recyclerView = viewBinding.recyclerViewCasts;
        layoutManager = (LinearLayoutManager) layoutManager(viewBinding.getRoot());
        layoutManager.setInitialPrefetchItemCount(4);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        layoutManager.scrollToPosition(scrollPos);
        long took = System.currentTimeMillis() - before;
        Log.e(TAG, "bind: took " + took + " ms");
    }
public static final String TAG = ItemListHorizontal.class.getSimpleName();
    @Override
    public void unbind(ViewHolder<RowCastListBinding> holder) {
        Log.d(TAG, "unbind() called with: holder = [" + holder + "]");
        RecyclerView recyclerView = holder.binding.recyclerViewCasts;
        recyclerView.setOnFlingListener(null);
        recyclerView.setLayoutManager(null);
        super.unbind(holder);
    }

    @NonNull
    protected RecyclerView.LayoutManager layoutManager(View root) {
        return new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    public void onSaveViewState(Bundle outState) {
        outState.putInt(SCROLL_POS, layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0);
    }

    public void onRestoreViewState(Bundle savedViewState) {
        scrollPos = savedViewState.getInt(SCROLL_POS);
    }

    public void onDestroyView() {
        adapter.clear();
    }
}
