package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardListBinding;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardList extends Item<RowCardListBinding> {

    private final String POPULAR_IS_LOADING;
    private final String POPULAR_SCROLL_POS;

    private final PublishSubject<Integer> observer = PublishSubject.create();
    @NonNull
    private final String tag;
    //    private final SnapHelper snapHelper = new StartSnapHelper();
    private LinearLayoutManager layoutManager;
    private final ItemDecorationMargin margin;
    private GroupAdapter adapter;

    private boolean isLoading = false;
    private Integer page = 1;
    private int scrollPos;
    private Disposable disposable;

    public ItemCardList(
            @NonNull Context context,
            @NonNull GroupAdapter adapter,
            @NonNull String tag
    ) {
        this.tag = tag;
        context = context.getApplicationContext();
//        this.controller = controller;
        this.adapter = adapter;
        this.margin = new ItemDecorationMargin(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin));

        POPULAR_SCROLL_POS = "POPULAR_SCROLL_POS_" + tag;
        POPULAR_IS_LOADING = "POPULAR_IS_LOADING_" + tag;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_list;
    }

    @Override
    public void bind(RowCardListBinding binding, int position) {
        Log.d(TAG + tag, "bind() called with: binding = [" + binding + "], position = [" + position + "]");
        RecyclerView recyclerView = binding.recyclerViewCard;
        layoutManager = new LinearLayoutManager(binding.recyclerViewCard.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(margin);
        recyclerView.setAdapter(adapter);
//        snapHelper.attachToRecyclerView(recyclerView);
        layoutManager.scrollToPosition(scrollPos);
        disposable = registerLoadMoreTrigger(recyclerView).subscribe();
    }

    @Override
    public void unbind(ViewHolder<RowCardListBinding> holder) {
        Log.d(TAG + tag, "unbind() called with: holder = [" + holder + "]");
        RecyclerView recyclerView = holder.binding.recyclerViewCard;
        recyclerView.removeItemDecoration(margin);
        recyclerView.setOnFlingListener(null);
        recyclerView.setLayoutManager(null);
        layoutManager = null;
        UtilsRx.dispose(disposable);
        super.unbind(holder);
    }

    private static final String TAG = ItemCardList.class.getSimpleName();

    protected Observable<Integer> registerLoadMoreTrigger(RecyclerView recyclerView) {
        Log.d(TAG + tag, "registerLoadMoreTrigger called");
        return RxRecyclerView.scrollEvents(recyclerView)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        return !isLoading;
                    }
                })
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        return layoutManager.getItemCount() < layoutManager.findLastVisibleItemPosition() + 2;
                    }
                })
                .map(new Function<RecyclerViewScrollEvent, Integer>() {
                    @Override
                    public Integer apply(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        return page;
                    }
                })
                .doOnNext(new Consumer<Integer>() {

                    @Override
                    public void accept(@NonNull Integer page) throws Exception {
                        isLoading = true; observer.onNext(page);
                    }
                });
    }

    public void onSaveViewState(View view, Bundle outState) {
        Log.d(TAG + tag, "onSaveViewState");
        outState.putInt(POPULAR_SCROLL_POS, layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0);
        outState.putBoolean(POPULAR_IS_LOADING, isLoading);
    }

    public void onRestoreViewState(View view, Bundle savedViewState) {
        Log.d(TAG + tag, "onRestoreViewState");
        isLoading = savedViewState.getBoolean(POPULAR_IS_LOADING, isLoading);
        scrollPos = savedViewState.getInt(POPULAR_SCROLL_POS);
    }

    public void loading(boolean loading) {
        this.isLoading = loading;
    }

    public void page(int page) {
        this.page = page;
    }

    public PublishSubject<Integer> observer() {
        return observer;
    }

    public void onDestroyView() {
        UtilsRx.dispose(disposable);
        adapter.setOnItemClickListener(null);
        adapter.clear();
//        adapter = null;
        layoutManager = null;
    }
}
