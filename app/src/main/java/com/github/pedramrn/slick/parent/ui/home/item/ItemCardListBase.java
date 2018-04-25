package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.RecycledViewPool;
import android.view.View;

import com.bluelinelabs.conductor.Router;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListAbs;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public abstract class ItemCardListBase<T extends ViewDataBinding, RecyclerView> extends Item<T> {
    private static final String TAG = ItemCardListBase.class.getSimpleName();

    private final Context context;
    private final String SCROLL_POS;
    private final RecycledViewPool viewPool;

    private LinearLayoutManager layoutManager;
    private final ItemDecorationMargin margin;
    //    private final SnapHelper snapHelper = new StartSnapHelper();

    private RecyclerViewCardListAbs recyclerView;
    private Router router;
    private int scrollPos;

    public ItemCardListBase(
            @NonNull Context context, @NonNull String tag, RecycledViewPool viewPool
    ) {
        this.context = context.getApplicationContext();
        this.margin = new ItemDecorationMargin(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin));
        this.viewPool = viewPool;
        SCROLL_POS = "SCROLL_POS_" + tag;
    }

    @Override
    public void bind(T binding, int position) {
        System.out.println("ItemCardListBase.bind");
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setInitialPrefetchItemCount(4);
        recyclerView = getRecyclerViewCardListAbs(binding);
        recyclerView.setRecycledViewPool(viewPool);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(margin);
        // TODO: 2018-04-25 commented out
        // recyclerView.setNavigator(router);
        recyclerView.setScrollPosition(scrollPos);
//        snapHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void unbind(ViewHolder<T> holder) {
        System.out.println("ItemCardListBase.unbind");
        recyclerView = getRecyclerViewCardListAbs(holder.binding);
        recyclerView.removeItemDecoration(margin);
        recyclerView.setOnFlingListener(null);
        recyclerView.setLayoutManager(null);
        super.unbind(holder);
    }

    protected abstract RecyclerViewCardListAbs getRecyclerViewCardListAbs(T binding);

    public void onSaveViewState(View view, Bundle outState) {
        outState.putInt(SCROLL_POS, layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0);
    }

    public void onRestoreViewState(View view, Bundle savedViewState) {
        scrollPos = savedViewState.getInt(SCROLL_POS);
    }

    public void setRouter(Router router) {this.router = router;}

    public void onDestroyView() {
        System.out.println("ItemCardListBase.onDestroyView");
        setRouter(null);
        layoutManager = null;
    }

    public void onDestroy() {
        System.out.println("ItemCardListBase.onDestroy");
        recyclerView = null;
    }
}
