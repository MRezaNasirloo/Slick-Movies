package com.github.pedramrn.slick.parent.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Router;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.home.item.ItemAnticipatedList;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ControllerHome extends ControllerBase implements ViewHome, Observer<ViewStateHome> {

    @Inject
    Provider<PresenterHome> provider;
    @Presenter
    PresenterHome presenter;
    private UpdatingGroup progressiveTrending;
    private UpdatingGroup progressivePopular;
    private ItemCardList itemTrendingList;
    private ItemCardList itemPopularList;
    private Disposable disposable;
    private OnItemClickListenerDetails onItemClickListener = new OnItemClickListenerDetails(new RouterProvider() {
        @Override
        public Router get() {
            return getRouter();
        }
    });
    private UpdatingGroup progressiveAnticipated;


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);

        GroupAdapter adapterMain = new GroupAdapter();
        GroupAdapter adapterAnticipated = new GroupAdapter();
        GroupAdapter adapterTrending = new GroupAdapter();
        GroupAdapter adapterPopular = new GroupAdapter();
        progressiveAnticipated = new UpdatingGroup();
        progressiveTrending = new UpdatingGroup();
        progressivePopular = new UpdatingGroup();
        itemTrendingList = new ItemCardList(getActivity(), adapterTrending, "trending", presenter.onLoadMoreObserverTrending(), onItemClickListener);
        itemPopularList = new ItemCardList(getActivity(), adapterPopular, "popular", presenter.onLoadMoreObserverPoplar(), onItemClickListener);

        ItemAnticipatedList itemAnticipatedList = new ItemAnticipatedList(adapterAnticipated);

        adapterMain.add(itemAnticipatedList);
        adapterAnticipated.add(progressiveAnticipated);

        PublishSubject<Object> onClickListener = PublishSubject.create();
        Section sectionTrending = new Section(new ItemCardHeader(1, "Trending", "See All", onClickListener));

        adapterTrending.add(progressiveTrending);
        sectionTrending.add(itemTrendingList);

        Section sectionPopular = new Section(new ItemCardHeader(1, "Popular", "See All", onClickListener));

        adapterPopular.add(progressivePopular);
        sectionPopular.add(itemPopularList);

        adapterMain.add(sectionTrending);
        adapterMain.add(sectionPopular);

        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewHome.setAdapter(adapterMain);
        setToolbar(binding.toolbar);

        int pageSize = getResources().getInteger(R.integer.page_size);

        Observable<Object> observable = onClickListener.doOnNext(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Toast.makeText(getApplicationContext(), "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });
        presenter.updateStream(pageSize, observable).subscribe(this);

        return binding.getRoot();
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        itemTrendingList.onSaveViewState(view, outState);
        itemPopularList.onSaveViewState(view, outState);
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        itemTrendingList.onRestoreViewState(view, savedViewState);
        itemPopularList.onRestoreViewState(view, savedViewState);
    }

    private static final String TAG = ControllerHome.class.getSimpleName();

    @Override
    public void render(@NonNull ViewStateHome state) {
        Log.d(TAG, "render() called");
        progressiveAnticipated.update(state.upcoming());
        progressiveTrending.update(state.trending());
        progressivePopular.update(state.popular());
        itemTrendingList.loading(state.loadingTrending());
        itemTrendingList.itemLoadedCount(state.itemLoadingCountTrending());
        itemTrendingList.page(state.pageTrending());
        itemPopularList.loading(state.loadingPopular());
        itemPopularList.itemLoadedCount(state.itemLoadingCountPopular());
        itemPopularList.page(state.pagePopular());


        // TODO: 2017-07-01 You're better than this...
        renderError(state.errorVideos());
        renderError(state.errorUpcoming());
        renderError(state.error());

    }

    @Override
    public void onSubscribe(Disposable d) {
        dispose(disposable);
        this.disposable = d;
    }

    @Override
    public void onNext(ViewStateHome viewStateHome) {
        render(viewStateHome);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.wtf(TAG, "onCompleteGlide() called x_X");
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        Log.d(TAG, "onDestroyView() called");
        super.onDestroyView(view);
        dispose(disposable);
    }

}
