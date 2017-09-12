package com.github.pedramrn.slick.parent.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontalPager;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.ui.search.SearchViewImpl;
import com.github.pedramrn.slick.parent.ui.videos.ControllerVideos;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
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
    private ItemCardList itemListTrending;
    private ItemCardList itemListPopular;
    private UpdatingGroup progressiveUpcoming;
    private ItemListHorizontal itemListUpcoming;

    private final RouterProvider routerProvider = new RouterProvider() {
        @Override
        public Router get() {
            return getRouter();
        }
    };
    private OnItemClickListenerAction actionDetails = new OnItemClickListenerAction(routerProvider, new ControllerProvider() {
        @Override
        public Controller get(MovieBasic movie, String transitionName) {
            return new ControllerDetails(movie, transitionName);
        }
    });
    private OnItemClickListenerAction actionVideos = new OnItemClickListenerAction(routerProvider, new ControllerProvider() {
        @Override
        public Controller get(MovieBasic movie, String transitionName) {
            return new ControllerVideos(movie, transitionName);
        }
    });

    private SearchViewImpl searchView;
    private ViewStateHome state;

    private PublishSubject<Object> onRetryTrending = PublishSubject.create();
    private PublishSubject<Object> onRetryUpcoming = PublishSubject.create();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
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
        itemListTrending = new ItemCardList(getActivity(), adapterTrending, "trending", actionDetails);
        itemListPopular = new ItemCardList(getActivity(), adapterPopular, "popular", actionDetails);

        itemListUpcoming = new ItemListHorizontalPager(getActivity(), adapterUpcoming, "UPCOMING", actionVideos);

        adapterUpcoming.add(progressiveUpcoming);
        Section sectionUpcoming = new Section(new ItemCardHeader(1, "UPCOMING", "See All", null));
        sectionUpcoming.add(itemListUpcoming);

        Consumer<Object> onClickListener = new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Toast.makeText(getApplicationContext(), "Under Construction...", Toast.LENGTH_SHORT).show();
            }
        };
        Section sectionTrending = new Section(new ItemCardHeader(1, "TRENDING", "See All", onClickListener));

        adapterTrending.add(progressiveTrending);
        sectionTrending.add(itemListTrending);

        Section sectionPopular = new Section(new ItemCardHeader(1, "Popular", "See All", onClickListener));

        adapterPopular.add(progressivePopular);
        sectionPopular.add(itemListPopular);

        adapterMain.add(sectionUpcoming);
        adapterMain.add(sectionTrending);
        adapterMain.add(sectionPopular);

        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewHome.setAdapter(adapterMain);
        // setToolbar(binding.toolbar);
        // binding.searchView.setOnQueryTextListener();
        searchView = binding.searchView;
        ((GroupAdapter) searchView.getAdapter()).setOnItemClickListener(actionDetails);

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
        this.state = state;
        Log.d(TAG, "render() called");
        progressiveUpcoming.update(state.upcoming());
        progressiveTrending.update(Arrays.asList(state.trending().values().toArray(new Item[state.trending().size()])));
        progressivePopular.update(state.popular());
        itemListTrending.loading(state.loadingTrending());
        itemListTrending.itemLoadedCount(state.itemLoadingCountTrending());
        itemListTrending.page(state.pageTrending());
        itemListPopular.loading(state.loadingPopular());
        itemListPopular.itemLoadedCount(state.itemLoadingCountPopular());
        itemListPopular.page(state.pagePopular());

        // TODO: 2017-07-01 You're better than this... IKR Look what I just made :))))
        renderError(state.errorVideos());
//        renderError(state.errorUpcoming());
//        renderError(state.error()); //handled
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
        return itemListPopular.observer().startWith(1);
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
