package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardListBinding;
import com.github.pedramrn.slick.parent.ui.custom.StartSnapHelper;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationSideMargin;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.xwray.groupie.Item;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardList extends Item<RowCardListBinding> {

    private final String POPULAR_PAGE;
    private final String POPULAR_IS_LOADING;
    private final String POPULAR_SCROLL_POS;
    private final RecyclerView.Adapter adapter;
    private boolean isLoading = false;
    private Integer page = 1;
    private LinearLayoutManager layoutManager;

    private int scrollPos;
    private int itemLoadedCount;
    private final Observer<Integer> observer;

    public ItemCardList(RecyclerView.Adapter adapter, String tag, Observer<Integer> observer) {
        this.adapter = adapter;
        POPULAR_PAGE = "POPULAR_PAGE_" + tag;
        POPULAR_IS_LOADING = "POPULAR_IS_LOADING_" + tag;
        POPULAR_SCROLL_POS = "POPULAR_SCROLL_POS_" + tag;
        this.observer = observer;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_list;
    }

    @Override
    public void bind(RowCardListBinding binding, int position) {
        Log.d(TAG, "ItemCardList bind() called");
        Context context = binding.getRoot().getContext();
        RecyclerView recyclerView = binding.recyclerViewCard;
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new ItemDecorationSideMargin(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin)));
        SnapHelper snapHelper = new StartSnapHelper();
        // FIXME: 2017-07-15
        /*java.lang.IllegalStateException: An instance of OnFlingListener already set.
                      at android.support.v7.widget.SnapHelper.setupCallbacks(SnapHelper.java:114)*/
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        layoutManager.scrollToPosition(scrollPos);
        registerLoadMoreTrigger(recyclerView).subscribe(observer);


        // TODO: 2017-07-04 the page number and the lading state should be saved during orientation change


    }

    private static final String TAG = ItemCardList.class.getSimpleName();

    protected Observable<Integer> registerLoadMoreTrigger(RecyclerView recyclerView) {
        return RxRecyclerView.scrollEvents(recyclerView)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        Log.d(TAG, "isLoading() called with: event = [" + isLoading + "]");
                        return !isLoading;
                    }
                })
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        boolean b = layoutManager.getItemCount() <= itemLoadedCount;
                        Log.d(TAG, "itemLoadedCount() called with: event = [" + b + "]");
                        return b;
                    }
                })
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        int totalItemCount = layoutManager.getItemCount();
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        boolean b = totalItemCount < (lastVisibleItem + 2);
                        Log.d(TAG, "shouldWeLoad() called with: event = [" + b + "]");
                        return b;
                    }
                })
                .map(new Function<RecyclerViewScrollEvent, Integer>() {
                    @Override
                    public Integer apply(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        return page + 1;
                    }
                })
                .doOnNext(new Consumer<Integer>() {

                    @Override
                    public void accept(@NonNull Integer page) throws Exception {
                        isLoading = true;
                        Log.d(TAG, "accept() called with: page = [" + page + "]");
                    }
                });
        // .throttleLast(1, TimeUnit.SECONDS)
    }

    public void onSaveViewState(View view, Bundle outState) {
        outState.putInt(POPULAR_PAGE, page);
        outState.putBoolean(POPULAR_IS_LOADING, isLoading);
        outState.putInt(POPULAR_SCROLL_POS, layoutManager != null ? layoutManager.findFirstCompletelyVisibleItemPosition() : 0);
    }

    public void onRestoreViewState(View view, Bundle savedViewState) {
        page = savedViewState.getInt(POPULAR_PAGE, page);
        isLoading = savedViewState.getBoolean(POPULAR_IS_LOADING, isLoading);
        scrollPos = savedViewState.getInt(POPULAR_SCROLL_POS);
    }

    public void loading(boolean loading) {
        Log.d(TAG, "loading() called load again");
        this.isLoading = loading;
    }

    public void itemLoadedCount(int itemLoadedCount) {
        this.itemLoadedCount = itemLoadedCount;
    }

    public void page(int page) {
        this.page = page;
    }
}
