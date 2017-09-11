package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardListBinding;
import com.github.pedramrn.slick.parent.ui.custom.StartSnapHelper;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
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

    private final String POPULAR_PAGE;
    private final String POPULAR_IS_LOADING;
    private final String POPULAR_SCROLL_POS;

    private final SnapHelper snapHelper = new StartSnapHelper();
    private final LinearLayoutManager layoutManager;
    private final ItemDecorationMargin margin;
    private final GroupAdapter adapter;
    private final OnItemClickListener onItemClickListener;
    private PublishSubject<Integer> observer = PublishSubject.create();


    private boolean isLoading = false;
    private Integer page = 1;
    private int scrollPos;
    private int itemLoadedCount;
    private Disposable disposable;

    public ItemCardList(
            @NonNull Context context,
            @NonNull GroupAdapter adapter,
            @NonNull String tag,
            @Nullable OnItemClickListener onItemClickListener
    ) {
        context = context.getApplicationContext();
        this.margin = new ItemDecorationMargin(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin));
        this.layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        this.onItemClickListener = onItemClickListener;
        this.adapter = adapter;

        POPULAR_SCROLL_POS = "POPULAR_SCROLL_POS_" + tag;
        POPULAR_IS_LOADING = "POPULAR_IS_LOADING_" + tag;
        POPULAR_PAGE = "POPULAR_PAGE_" + tag;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_list;
    }

    @Override
    public void bind(RowCardListBinding binding, int position) {
        Log.d(TAG, "bind() called with: binding = [" + binding + "], position = [" + position + "]");
        RecyclerView recyclerView = binding.recyclerViewCard;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(margin);
        recyclerView.setAdapter(adapter);
        snapHelper.attachToRecyclerView(recyclerView);
        layoutManager.scrollToPosition(scrollPos);
        disposable = registerLoadMoreTrigger(recyclerView).subscribe();
        adapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void unbind(ViewHolder<RowCardListBinding> holder) {
        Log.d(TAG, "unbind() called with: holder = [" + holder + "]");
        RecyclerView recyclerView = holder.binding.recyclerViewCard;
        recyclerView.removeItemDecoration(margin);
        recyclerView.setOnFlingListener(null);
        adapter.setOnItemClickListener(null);
        UtilsRx.dispose(disposable);
        super.unbind(holder);
    }

    private static final String TAG = ItemCardList.class.getSimpleName();

    protected Observable<Integer> registerLoadMoreTrigger(RecyclerView recyclerView) {
        Log.d(TAG, "registerLoadMoreTrigger called");
        return RxRecyclerView.scrollEvents(recyclerView)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        // Log.d(TAG, "isLoading() called with: event = [" + isLoading + "]");
                        return !isLoading;
                    }
                })
                /*.filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        boolean b = layoutManager.getItemCount() <= itemLoadedCount;
                        // Log.d(TAG, "itemLoadedCount() called with: event = [" + b + "]");
                        return b;
                    }
                })*/
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        int totalItemCount = layoutManager.getItemCount();
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        boolean b = totalItemCount < (lastVisibleItem + 2);
                        // Log.d(TAG, "shouldWeLoad() called with: event = [" + b + "]");
                        return b;
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
                        isLoading = true;
                        observer.onNext(page);
                        Log.d(TAG, "accept() called with: page = [" + page + "]");
                    }
                });
        // .throttleLast(1, TimeUnit.SECONDS)
    }

    public void onSaveViewState(View view, Bundle outState) {
        outState.putInt(POPULAR_SCROLL_POS, layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0);
        outState.putBoolean(POPULAR_IS_LOADING, isLoading);
    }

    public void onRestoreViewState(View view, Bundle savedViewState) {
        isLoading = savedViewState.getBoolean(POPULAR_IS_LOADING, isLoading);
        scrollPos = savedViewState.getInt(POPULAR_SCROLL_POS);
    }

    public void loading(boolean loading) {
        this.isLoading = loading;
    }

    public void itemLoadedCount(int itemLoadedCount) {
        this.itemLoadedCount = itemLoadedCount;
    }

    public void page(int page) {
        this.page = page;
    }

    public PublishSubject<Integer> observer() {
        return observer;
    }
}
