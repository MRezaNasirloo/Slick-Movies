package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMarginSide;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.mrezanasirloo.slick.SlickLifecycleListener;
import com.mrezanasirloo.slick.SlickUniqueId;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-09-16
 * <p>
 * A base Recycler View for loaing a list of movies progressivly
 */
public abstract class RecyclerViewCardListAbs extends RecyclerView implements ViewCardList, OnItemClickListener,
        Retryable, SlickLifecycleListener, SlickUniqueId {

    public static final String TAG = RecyclerViewCardListAbs.class.getSimpleName();

    private PublishSubject<String> onRetry = PublishSubject.create();
    private boolean isLoading;
    private AdapterLightWeight adapter;

    private ItemDecoration margin;
    private Navigator navigator;

    public RecyclerViewCardListAbs(Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewCardListAbs(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerViewCardListAbs(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void init(Context context) {
        this.margin = new ItemDecorationMarginSide(context.getResources().getDimensionPixelSize(R.dimen.card_list_side_margin));
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
        getItemAnimator().setChangeDuration(0);
        setNestedScrollingEnabled(false);
        adapter = new AdapterLightWeight();
        adapter.setOnItemClickListener(this);
        adapter.setHasStableIds(true);
        addItemDecoration(margin);
        setAdapter(adapter);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e(TAG, "onDetachedFromWindow");
        if (adapter != null) {
            adapter.setOnItemClickListener(null);
            adapter = null;
        }
        removeItemDecoration(margin);
        super.onDetachedFromWindow();
    }

    @Override
    public void onRetry(String tag) {
        onRetry.onNext(tag);
    }

    @Override
    public void loading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public void updateList(List<Item> items) {
        Log.d(TAG, "updateList: called");
        adapter.update(items);
    }

    @Override
    public int pageSize() {
        return 6;
    }

    @NonNull
    @Override
    public Observable<Object> trigger() {
        return RxRecyclerView.scrollEvents(this)
                .filter(event -> !isLoading)
                .filter(event -> {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                    return layoutManager.getItemCount() < layoutManager.findLastVisibleItemPosition() + 2;
                })
                .cast(Object.class)
                .doOnNext(o -> isLoading = true).mergeWith(retry());
    }

    public Observable<String> retry() {
        return onRetry;
    }

    @Override
    public void onItemClick(Item item, View view) {
        ((OnItemAction) item).action(navigator, null, ((AdapterLightWeight) getAdapter()).getAdapterPosition(item), view);
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    protected String id;

    @NonNull
    @Override
    public String getUniqueId() {
        return id;
    }

    public void onDestroy() {
        navigator = null;
    }
}
