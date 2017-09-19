package com.github.pedramrn.slick.parent.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.RecycledViewPool;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontalPager;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardListBase;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardListPopular;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardListTrending;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.search.SearchViewImpl;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
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
    private ItemCardListBase itemListTrending;
    private ItemCardListBase itemListPopular;
    private ItemListHorizontal itemListUpcoming;

    private SearchViewImpl searchView;

    private PublishSubject<String> onRetry = PublishSubject.create();

    private static final String trending = "Trending";
    private static final String popular = "Popular";
    private static final String upcoming = "Upcoming";

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        Log.d(TAG, "onCreateView");
        App.componentMain().inject(this);
        ControllerHome_Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);

        GroupAdapter adapterMain = new GroupAdapter();
        GroupAdapter adapterUpcoming = new GroupAdapter();

        progressiveUpcoming = new UpdatingGroup();

        final Context context = getApplicationContext();

        RecycledViewPool recycledViewPool = new RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(R.layout.row_card, 12);
        itemListTrending = new ItemCardListTrending(context, trending, recycledViewPool);
        itemListPopular = new ItemCardListPopular(context, popular, recycledViewPool);
        itemListUpcoming = new ItemListHorizontalPager(context, adapterUpcoming, upcoming);

        itemListTrending.setRouter(getRouter());
        itemListPopular.setRouter(getRouter());

        Section sectionUpcoming = new Section(new ItemCardHeader(1, upcoming));
        sectionUpcoming.add(itemListUpcoming);

        Section sectionTrending = new Section(new ItemCardHeader(2, trending));
        sectionTrending.add(itemListTrending);

        Section sectionPopular = new Section(new ItemCardHeader(3, popular));
        sectionPopular.add(itemListPopular);

        adapterUpcoming.add(progressiveUpcoming);


        // setToolbar(binding.toolbar);
        searchView = binding.searchView;
        setOnItemClickListener(adapterUpcoming);
        setOnItemClickListener((GroupAdapter) searchView.getAdapter());

        adapterMain.add(sectionUpcoming);
        adapterMain.add(sectionTrending);
        adapterMain.add(sectionPopular);

        RecyclerView recyclerViewHome = binding.recyclerViewHome;
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewHome.setAdapter(adapterMain);
        recyclerViewHome.setItemViewCacheSize(2);

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        itemListTrending.onSaveViewState(view, outState);
        itemListPopular.onSaveViewState(view, outState);
        itemListUpcoming.onSaveViewState(view, outState);
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        itemListTrending.onRestoreViewState(view, savedViewState);
        itemListPopular.onRestoreViewState(view, savedViewState);
        itemListUpcoming.onRestoreViewState(view, savedViewState);
    }

    @Override
    public void render(@NonNull ViewStateHome state) {
        Log.d(TAG, "render() called");
        progressiveUpcoming.update(state.upcoming());
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        itemListTrending.onDestroyView();
        itemListPopular.onDestroyView();
        itemListUpcoming.onDestroyView();
        if (isBeingDestroyed()) {
            itemListTrending.onDestroy();
            itemListPopular.onDestroy();
            searchView.onDestroy();
        }
        itemListPopular = null;
        itemListTrending = null;
        itemListUpcoming = null;
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
        return onRetry.filter(new Predicate<String>() {
            @Override
            public boolean test(@NonNull String s) throws Exception {
                return PresenterHome.UPCOMING.equals(s);
            }
        }).cast(Object.class);
    }

    @Override
    public void onRetry(String tag) {
        onRetry.onNext(tag);
    }

    private void setOnItemClickListener(final GroupAdapter adapter) {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                ((OnItemAction) item).action(ControllerHome.this, null, adapter.getAdapterPosition(item));
            }
        });
    }
}
