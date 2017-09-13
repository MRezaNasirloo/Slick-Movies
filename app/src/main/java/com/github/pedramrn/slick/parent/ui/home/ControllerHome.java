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
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontalPager;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.search.SearchViewImpl;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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

    private UpdatingGroup progressiveTrending;
    private UpdatingGroup progressivePopular;
    private UpdatingGroup progressiveUpcoming;
    private ItemCardList itemListTrending;
    private ItemCardList itemListPopular;
    private ItemListHorizontal itemListUpcoming;

    private SearchViewImpl searchView;
    private ViewStateHome state;

    private PublishSubject<Object> onRetryTrending = PublishSubject.create();
    private PublishSubject<Object> onRetryUpcoming = PublishSubject.create();

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
        GroupAdapter adapterTrending = new GroupAdapter();
        GroupAdapter adapterPopular = new GroupAdapter();

        progressiveUpcoming = new UpdatingGroup();
        progressiveTrending = new UpdatingGroup();
        progressivePopular = new UpdatingGroup();

        final Context context = getApplicationContext();

        itemListTrending = new ItemCardList(context, adapterTrending, trending);
        itemListPopular = new ItemCardList(context, adapterPopular, popular);
        itemListUpcoming = new ItemListHorizontalPager(context, adapterUpcoming, upcoming);

        Section sectionUpcoming = new Section(new ItemCardHeader(1, upcoming));
        sectionUpcoming.add(itemListUpcoming);

        Section sectionTrending = new Section(new ItemCardHeader(1, trending));
        sectionTrending.add(itemListTrending);

        Section sectionPopular = new Section(new ItemCardHeader(1, popular));
        sectionPopular.add(itemListPopular);

        adapterUpcoming.add(progressiveUpcoming);
        adapterPopular.add(progressivePopular);
        adapterTrending.add(progressiveTrending);


        // setToolbar(binding.toolbar);
        searchView = binding.searchView;
        setOnItemClickListener(adapterUpcoming);
        setOnItemClickListener(adapterTrending);
        setOnItemClickListener(adapterPopular);
        setOnItemClickListener((GroupAdapter) searchView.getAdapter());

        adapterMain.add(sectionUpcoming);
        adapterMain.add(sectionTrending);
        adapterMain.add(sectionPopular);

        RecyclerView recyclerViewHome = binding.recyclerViewHome;
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewHome.setAdapter(adapterMain);

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    private void setOnItemClickListener(final GroupAdapter adapter) {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                ((OnItemAction) item).action(ControllerHome.this, adapter.getAdapterPosition(item));
            }
        });
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
        this.state = state;
        Log.d(TAG, "render() called");
        progressiveUpcoming.update(state.upcoming());
        progressiveTrending.update(Arrays.asList(state.trending().values().toArray(new Item[state.trending().size()])));
        progressivePopular.update(state.popular());

        itemListTrending.loading(state.loadingTrending());
        itemListTrending.page(state.pageTrending());
        itemListPopular.loading(state.loadingPopular());
        itemListPopular.page(state.pagePopular());

    }

    @Override
    protected void onAttach(@NonNull View view) {
        Log.d(TAG, "onAttach");
        super.onAttach(view);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        Log.d(TAG, "onDetach");
        super.onDetach(view);
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView(view);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
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
    public Observable<Integer> triggerTrending() {
        System.out.println("PresenterHome.triggerTrending");
//        return Observable.just(1);
        return itemListTrending.observer().doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                System.out.println("PresenterHome.onNext [" + integer + "]");
            }
        }).mergeWith(retryTrending());
    }

    @Override
    public Observable<Integer> triggerPopular() {
        System.out.println("PresenterHome.triggerPopular");
//        return Observable.just(1);
        return itemListPopular.observer();
    }

    @Override
    public int pageSize() {
        System.out.println("PresenterHome.pageSize");
        return 3;
    }

    @Override
    public Observable<Integer> retryTrending() {
        System.out.println("PresenterHome.retryTrending");
        return onRetryTrending.map(new Function<Object, Integer>() {
            @Override
            public Integer apply(@NonNull Object o) throws Exception {
                return state.pageTrending();
            }
        });
        /*Observable.intervalRange(0, 100, 3, 3, TimeUnit.SECONDS).cast(Object.class);*/
    }

    @Override
    public Observable<Object> retryUpcoming() {
        return onRetryUpcoming;
    }

    public void onClickRetryTrending(Object o) {
        System.out.println("ControllerHome.onClickRetryTrending");
        onRetryTrending.onNext(o);
    }

    public void onClickRetryUpcoming(Object o) {
        System.out.println("ControllerHome.onClickRetryUpcoming");
        onRetryUpcoming.onNext(o);
    }
}
