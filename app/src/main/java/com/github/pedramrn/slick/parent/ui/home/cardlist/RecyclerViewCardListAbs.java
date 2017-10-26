package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMarginSide;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.slick.OnDestroyListener;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-16
 *         <p>
 *         A base Recycler View for loaing a list of movies progressivly
 */
public abstract class RecyclerViewCardListAbs extends RecyclerView implements ViewCardList, OnItemClickListener,
        Navigator, Retryable, OnDestroyListener {

    public static final String TAG = RecyclerViewCardListAbs.class.getSimpleName();
    private static final String SCROLL_POS = "SCROLL_POS_";

    private PublishSubject<String> onRetry = PublishSubject.create();
    private boolean isLoading;
    private Router router;
    private AdapterLightWeight adapter;

    protected int scrollPosition;
    private ItemDecoration margin;

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
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        Log.d(TAG, "onDetachedFromWindow: X: Y: " + layoutManager.findFirstVisibleItemPosition());
        Log.d(TAG, "onDetachedFromWindow");
        adapter.setOnItemClickListener(null);
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
        return 3;
    }

    @Override
    public Observable<Object> trigger() {
        return RxRecyclerView.scrollEvents(this)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        return !isLoading;
                    }
                })
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                        return layoutManager.getItemCount() < layoutManager.findLastVisibleItemPosition() + 2;
                    }
                })
                .cast(Object.class)
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        isLoading = true;
                    }
                }).mergeWith(retry());
    }

    public Observable<String> retry() {
        return onRetry;
    }

    @Override
    public void onItemClick(Item item, View view) {
        ((OnItemAction) item).action(this, null, ((AdapterLightWeight) getAdapter()).getAdapterPosition(item));
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public void navigateTo(Controller controller) {
        getRouter().pushController(RouterTransaction.with(controller)
                .popChangeHandler(new HorizontalChangeHandler())
                .pushChangeHandler(new HorizontalChangeHandler()));
    }

    @Override
    public View getView() {
        return getRootView();
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    public void onSaveViewState(View view, Bundle outState, String tag) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        outState.putInt(SCROLL_POS + tag, layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0);
    }

    public void onRestoreViewState(View view, Bundle savedViewState, String tag) {
        scrollPosition = savedViewState.getInt(SCROLL_POS + tag, 0);
        if (scrollPosition > 0) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            layoutManager.scrollToPosition(scrollPosition);
        }

    }
}
