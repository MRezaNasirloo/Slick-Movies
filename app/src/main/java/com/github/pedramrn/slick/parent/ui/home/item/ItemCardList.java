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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardList extends Item<RowCardListBinding> {

    private final String key = String.valueOf(UUID.randomUUID().toString().hashCode());
    private final String POPULAR_PAGE = "POPULAR_PAGE_" + key;
    private final String POPULAR_IS_LOADING = "POPULAR_IS_LOADING_" + key;
    private final String POPULAR_SCROLL_POS = "POPULAR_SCROLL_POS_" + key;
    private final RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    private Integer page = 0;
    private LinearLayoutManager layoutManager;

    private PublishSubject<Integer> trigger = PublishSubject.create();
    private int scrollPos;

    public ItemCardList(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_list;
    }

    @Override
    public void bind(RowCardListBinding viewBinding, int position) {
        Log.d(TAG, "ItemCardList bind() called");
        Context context = viewBinding.getRoot().getContext();
        recyclerView = viewBinding.recyclerViewCard;
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new ItemDecorationSideMargin(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin)));
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        layoutManager.scrollToPosition(scrollPos);


        // TODO: 2017-07-04 the page number and the lading state should be saved during orientation change
        RxRecyclerView.scrollEvents(recyclerView)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        // Log.d(TAG, "!isLoading() called " + !isLoading);
                        return !isLoading;
                    }
                })
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        int totalItemCount = layoutManager.getItemCount();
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        return totalItemCount < (lastVisibleItem + 2);
                    }
                })
                .scan(page + 1, new BiFunction<Integer, RecyclerViewScrollEvent, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer page, @NonNull RecyclerViewScrollEvent event) throws Exception {
                        return ++page;
                    }
                })
                .doOnNext(new Consumer<Integer>() {

                    @Override
                    public void accept(@NonNull Integer page) throws Exception {
                        ItemCardList.this.page = page;
                        isLoading = true;
                        Log.d(TAG, "accept() called with: page = [" + page + "]");
                    }
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(trigger);

    }

    private static final String TAG = ItemCardList.class.getSimpleName();

    public Observable<Integer> getPageTrigger() {
        return trigger;
    }

    public void onSaveViewState(View view, Bundle outState) {
        outState.putInt(POPULAR_PAGE, page);
        outState.putBoolean(POPULAR_IS_LOADING, isLoading);
        outState.putInt(POPULAR_SCROLL_POS, layoutManager.findFirstCompletelyVisibleItemPosition());
    }

    public void onRestoreViewState(View view, Bundle savedViewState) {
        page = savedViewState.getInt(POPULAR_PAGE, page);
        isLoading = savedViewState.getBoolean(POPULAR_IS_LOADING, isLoading);
        scrollPos = savedViewState.getInt(POPULAR_SCROLL_POS);
    }

    public void setLoading(boolean loading) {
        Log.d(TAG, "setLoading() called load again");
        this.isLoading = loading;
    }
}
