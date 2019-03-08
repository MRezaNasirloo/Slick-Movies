package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardListBinding;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.databinding.ViewHolder;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardList extends BindableItem<RowCardListBinding> {

    private final String IS_LOADING;
    private final String SCROLL_POS;

    private final PublishSubject<Integer> observer = PublishSubject.create();
    @NonNull
    private final String tag;
    private final Context context;
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
        this.context = context.getApplicationContext();
        this.tag = tag;
        this.adapter = adapter;
        this.margin = new ItemDecorationMargin(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin));
        SCROLL_POS = "SCROLL_POS_" + tag;
        IS_LOADING = "IS_LOADING_" + tag;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_list;
    }

    @Override
    public void bind(@NonNull RowCardListBinding binding, int position) {
        Log.d(TAG + tag, "bind() called with: binding = [" + binding + "], position = [" + position + "]");
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = binding.recyclerViewCard;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(margin);
        recyclerView.setAdapter(adapter);
        // snapHelper.attachToRecyclerView(recyclerView);
        layoutManager.scrollToPosition(scrollPos);
        disposable = registerLoadMoreTrigger(recyclerView).subscribe();
    }

    @Override
    public void unbind(@NonNull ViewHolder<RowCardListBinding> holder) {
        Log.d(TAG + tag, "unbind() called with: holder = [" + holder + "]");
        RecyclerView recyclerView = holder.binding.recyclerViewCard;
        recyclerView.removeItemDecoration(margin);
        recyclerView.setOnFlingListener(null);
        recyclerView.setLayoutManager(null);
        UtilsRx.dispose(disposable);
        super.unbind(holder);
    }

    private static final String TAG = ItemCardList.class.getSimpleName();

    protected Observable<Integer> registerLoadMoreTrigger(RecyclerView recyclerView) {
        Log.d(TAG + tag, "registerLoadMoreTrigger called");
        return RxRecyclerView.scrollEvents(recyclerView)
                .filter(event -> !isLoading)
                .filter(event -> layoutManager.getItemCount() < layoutManager.findLastVisibleItemPosition() + 2)
                .map(event -> page)
                .doOnNext(page -> {
                    isLoading = true; observer.onNext(page);
                });
    }

    public void onSaveViewState(Bundle outState) {
        Log.d(TAG + tag, "onSaveViewState");
        outState.putInt(SCROLL_POS, layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0);
        outState.putBoolean(IS_LOADING, isLoading);
    }

    public void onRestoreViewState(Bundle savedViewState) {
        Log.d(TAG + tag, "onRestoreViewState");
        isLoading = savedViewState.getBoolean(IS_LOADING, isLoading);
        scrollPos = savedViewState.getInt(SCROLL_POS);
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

    public void onDestroyView(){
        adapter.clear();
    }
}
