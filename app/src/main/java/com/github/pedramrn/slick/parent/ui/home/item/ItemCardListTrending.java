package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.bluelinelabs.conductor.Router;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardListTrendingBinding;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.ui.home.PresenterHome;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListTrending;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardListTrending extends Item<RowCardListTrendingBinding> {
    private static final String TAG = ItemCardListTrending.class.getSimpleName();

    private final String SCROLL_POS;

    private final Context context;
    //    private final SnapHelper snapHelper = new StartSnapHelper();
    private LinearLayoutManager layoutManager;
    private final ItemDecorationMargin margin;

    private int scrollPos;
    private RecyclerViewCardListTrending recyclerView;
    private Router router;

    public ItemCardListTrending(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.margin = new ItemDecorationMargin(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin));
        SCROLL_POS = "SCROLL_POS_" + PresenterHome.TRENDING;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_list_trending;
    }

    @Override
    public void bind(RowCardListTrendingBinding binding, int position) {
        Log.d(TAG, "bind() called with: binding = [" + binding + "], position = [" + position + "]");
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(margin);
        recyclerView.setRouter(router);
//        snapHelper.attachToRecyclerView(recyclerView);
//        layoutManager.scrollToPosition(scrollPos);
    }

    @Override
    public void unbind(ViewHolder<RowCardListTrendingBinding> holder) {
        Log.d(TAG, "unbind() called with: holder = [" + holder + "]");
        recyclerView = holder.binding.recyclerView;
        recyclerView.removeItemDecoration(margin);
        recyclerView.setOnFlingListener(null);
        recyclerView.setLayoutManager(null);
        super.unbind(holder);
    }


    public void onSaveViewState(View view, Bundle outState) {
        Log.d(TAG, "onSaveViewState");
        outState.putInt(SCROLL_POS, layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0);
    }

    public void onRestoreViewState(View view, Bundle savedViewState) {
        Log.d(TAG, "onRestoreViewState");
        scrollPos = savedViewState.getInt(SCROLL_POS);
    }

    public void setRouter(Router router) {this.router = router;}

    public void onDestroyView() {
        recyclerView.setRouter(null);
        layoutManager = null;
        recyclerView = null;
        setRouter(null);
//        margin = null;
    }
}
