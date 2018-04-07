package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.mrezanasirloo.slick.OnDestroyListener;
import com.mrezanasirloo.slick.SlickUniqueId;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.mrezanasirloo.slick.SlickDelegateActivity.SLICK_UNIQUE_KEY;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-16
 *         <p>
 *         A base Recycler View for loaing a list of movies progressivly
 */
public abstract class RecyclerViewCardListAbs extends RecyclerView implements ViewCardList, OnItemClickListener,
        Navigator, Retryable, OnDestroyListener, SlickUniqueId {

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
        if (adapter != null) {
            adapter.setOnItemClickListener(null);
            adapter = null;
        }
        removeItemDecoration(margin);
        super.onDetachedFromWindow();
    }

    @Override
    public void onDestroy() {
        if (adapter == null) return;
        adapter.setOnItemClickListener(null);
        adapter = null;
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

    @Nullable
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString(SLICK_UNIQUE_KEY, this.id);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.id = bundle.getString(SLICK_UNIQUE_KEY);
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    private String id;

    @Override
    public String getUniqueId() {
        return id = (id != null ? id : UUID.randomUUID().toString());
    }
}
