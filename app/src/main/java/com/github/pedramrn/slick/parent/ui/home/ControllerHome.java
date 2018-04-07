package com.github.pedramrn.slick.parent.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.databinding.RowCardHeaderBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListPopular;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListTrending;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.search.SearchViewImpl;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.mrezanasirloo.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.UpdatingGroup;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ControllerHome extends ControllerElm<ViewStateHome> implements ViewHome {

    private static final String TAG = ControllerHome.class.getSimpleName();

    @Inject
    Provider<PresenterHome> provider;
    @Presenter
    PresenterHome presenter;

    private UpdatingGroup progressiveUpcoming;

    private SearchViewImpl searchView;

    private PublishSubject<String> onRetry = PublishSubject.create();

    private static final String trending = "Trending";
    private static final String popular = "Popular";
    private static final String upcoming = "Upcoming";
    private RecyclerViewCardListTrending recyclerViewCardListTrending;
    private RecyclerViewCardListPopular recyclerViewCardListPopular;
    private GroupAdapter adapterUpcoming;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        Log.d(TAG, "onCreateView");
        App.componentMain().inject(this);
        PresenterHome_Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);

        adapterUpcoming = new GroupAdapter();
        progressiveUpcoming = new UpdatingGroup();

        final Context context = getApplicationContext();

        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(R.layout.row_card, 12);
        recyclerViewCardListTrending = binding.recyclerViewTrending;
        recyclerViewCardListPopular = binding.recyclerViewPopular;

        recyclerViewCardListTrending.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCardListPopular.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCardListTrending.setRouter(getRouter());
        recyclerViewCardListPopular.setRouter(getRouter());
        recyclerViewCardListPopular.setRecycledViewPool(recycledViewPool);
        recyclerViewCardListTrending.setRecycledViewPool(recycledViewPool);

        SnapHelper snapHelperStartTrending = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelperStartPopular = new GravitySnapHelper(Gravity.START);
        snapHelperStartTrending.attachToRecyclerView(recyclerViewCardListTrending);
        snapHelperStartPopular.attachToRecyclerView(recyclerViewCardListPopular);

        adapterUpcoming.add(progressiveUpcoming);


        // setToolbar(binding.toolbar);
        searchView = binding.searchView;
        setOnItemClickListener(adapterUpcoming);
        setOnItemClickListener((GroupAdapter) searchView.getAdapter());

        RecyclerView recyclerViewUpcoming = binding.recyclerViewUpcoming;
        recyclerViewUpcoming.setNestedScrollingEnabled(false);
        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUpcoming.setLayoutManager(layout);
        recyclerViewUpcoming.setAdapter(adapterUpcoming);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerViewUpcoming);

        //setup headers
        setupHeader(binding.headerUpcoming, upcoming);
        setupHeader(binding.headerTrending, trending);
        setupHeader(binding.headerPopular, popular);

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    private void setupHeader(RowCardHeaderBinding header, String title, @Nullable Consumer<Object> clickListener) {
        if (clickListener == null) {
            header.button.setVisibility(View.INVISIBLE);
        } else {
            RxView.clicks(header.button).throttleLast(1, TimeUnit.SECONDS).subscribe(clickListener);
        }
        header.textViewTitle.setText(title);
    }

    private void setupHeader(RowCardHeaderBinding header, String title) {
        setupHeader(header, title, null);
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        recyclerViewCardListTrending.onSaveViewState(view, outState, PresenterCardList.TRENDING);
        recyclerViewCardListPopular.onSaveViewState(view, outState, PresenterCardList.POPULAR);
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        recyclerViewCardListTrending.onRestoreViewState(view, savedViewState, PresenterCardList.TRENDING);
        recyclerViewCardListPopular.onRestoreViewState(view, savedViewState, PresenterCardList.POPULAR);
    }

    @Override
    public void render(@NonNull ViewStateHome state) {
        Log.d(TAG, "render() called");
        progressiveUpcoming.update(state.upcoming());
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        if (isBeingDestroyed()) {
            recyclerViewCardListPopular.onDestroy();
            recyclerViewCardListTrending.onDestroy();
            searchView.onDestroy();
        }
        recyclerViewCardListPopular = null;
        recyclerViewCardListTrending = null;
        adapterUpcoming.setOnItemClickListener(null);
        adapterUpcoming = null;
        searchView = null;
    }

    @Override
    public void onSubscribe(Disposable d) {
        add(d);
    }

    @Override
    public void onNext(ViewStateHome viewStateHome) {
        render(viewStateHome);
    }

    @Override
    public void onError(Throwable e) {
        System.out.println("ControllerHome.onError");
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.wtf(TAG, "onComplete() called x_X");
    }

    @Override
    public boolean handleBack() {
        if (searchView.isSearchOpen()) {
            searchView.close(true);
            return true;
        }

        return super.handleBack();
    }

    @Override
    public Observable<Object> retryUpcoming() {
        return onRetry.filter(PresenterHome.UPCOMING::equals).cast(Object.class);
    }

    @Override
    public void onRetry(String tag) {
        onRetry.onNext(tag);
    }

    private void setOnItemClickListener(final GroupAdapter adapter) {
        adapter.setOnItemClickListener((item, view) -> ((OnItemAction) item).action(ControllerHome.this, null, adapter.getAdapterPosition(item)));
    }
}
