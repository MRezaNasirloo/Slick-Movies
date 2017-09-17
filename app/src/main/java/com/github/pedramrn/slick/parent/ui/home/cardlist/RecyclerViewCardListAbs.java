package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
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
public abstract class RecyclerViewCardListAbs extends RecyclerView implements ViewCardList, OnItemClickListener, Navigator, Retryable {

    private PublishSubject<String> onRetry = PublishSubject.create();
    private boolean isLoading;
    private Router router;
    private AdapterLightWeight adapter;

    public RecyclerViewCardListAbs(Context context) {
        super(context);
    }

    public RecyclerViewCardListAbs(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewCardListAbs(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getItemAnimator().setChangeDuration(0);
        setNestedScrollingEnabled(false);
        adapter = new AdapterLightWeight();
        adapter.setOnItemClickListener(this);
        setAdapter(adapter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        router = null;
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
}
