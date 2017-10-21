package com.github.pedramrn.slick.parent.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListPopular;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListTrending;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.search.SearchViewImpl;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
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
    private ItemListHorizontal itemListUpcoming;

    private SearchViewImpl searchView;

    private PublishSubject<String> onRetry = PublishSubject.create();

    private static final String trending = "Trending";
    private static final String popular = "Popular";
    private static final String upcoming = "Upcoming";
    private RecyclerViewCardListTrending recyclerViewCardListTrending;
    private RecyclerViewCardListPopular recyclerViewCardListPopular;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        Log.d(TAG, "onCreateView");
        App.componentMain().inject(this);
        ControllerHome_Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);

        GroupAdapter adapterUpcoming = new GroupAdapter();
        progressiveUpcoming = new UpdatingGroup();

        final Context context = getApplicationContext();

        // RecycledViewPool recycledViewPool = new RecycledViewPool();
        // recycledViewPool.setMaxRecycledViews(R.layout.row_card, 12);
        recyclerViewCardListTrending = binding.recyclerViewTrending;
        recyclerViewCardListPopular = binding.recyclerViewPopular;

        recyclerViewCardListTrending.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCardListPopular.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCardListTrending.setRouter(getRouter());
        recyclerViewCardListPopular.setRouter(getRouter());


//        Section sectionUpcoming = new Section(new ItemCardHeader(1, upcoming));
//        sectionUpcoming.add(itemListUpcoming);
        adapterUpcoming.add(progressiveUpcoming);

//        Section sectionTrending = new Section(new ItemCardHeader(2, trending));
//        sectionTrending.add(itemListTrending);

//        Section sectionPopular = new Section(new ItemCardHeader(3, popular));
//        sectionPopular.add(itemListPopular);



        // setToolbar(binding.toolbar);
        searchView = binding.searchView;
        setOnItemClickListener(adapterUpcoming);
        setOnItemClickListener((GroupAdapter) searchView.getAdapter());

//        adapterMain.add(sectionUpcoming);
//        adapterMain.add(sectionTrending);
//        adapterMain.add(sectionPopular);

        RecyclerView recyclerViewHome = binding.recyclerViewUpcoming;
        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHome.setLayoutManager(layout);
        recyclerViewHome.setAdapter(adapterUpcoming);

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        recyclerViewCardListTrending.onSaveViewState(view, outState, PresenterCardList.TRENDING);
        recyclerViewCardListPopular.onSaveViewState(view, outState, PresenterCardList.POPULAR);
        // itemListTrending.onSaveViewState(view, outState);
        // itemListPopular.onSaveViewState(view, outState);
        // itemListUpcoming.onSaveViewState(view, outState);
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        recyclerViewCardListTrending.onRestoreViewState(view, savedViewState, PresenterCardList.TRENDING);
        recyclerViewCardListPopular.onRestoreViewState(view, savedViewState, PresenterCardList.POPULAR);
//        itemListTrending.onRestoreViewState(view, savedViewState);
//        itemListPopular.onRestoreViewState(view, savedViewState);
        //         itemListUpcoming.onRestoreViewState(view, savedViewState);
    }

    @Override
    public void render(@NonNull ViewStateHome state) {
        Log.d(TAG, "render() called");
        progressiveUpcoming.update(state.upcoming());
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
//        itemListTrending.onDestroyView();
//        itemListPopular.onDestroyView();
        //         itemListUpcoming.onDestroyView();
        if (isBeingDestroyed()) {
            recyclerViewCardListPopular.onDestroy();
            recyclerViewCardListTrending.onDestroy();
//            itemListTrending.onDestroy();
//            itemListPopular.onDestroy();
            searchView.onDestroy();
        }
        // itemListPopular = null;
        // itemListTrending = null;
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
