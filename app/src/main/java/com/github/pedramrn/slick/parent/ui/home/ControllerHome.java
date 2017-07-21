package com.github.pedramrn.slick.parent.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Router;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontalPager;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ControllerHome extends ControllerBase implements ViewHome, Observer<ViewStateHome> {

    private static final String TAG = ControllerHome.class.getSimpleName();

    @Inject
    Provider<PresenterHome> provider;
    @Presenter
    PresenterHome presenter;
    private UpdatingGroup progressiveTrending;
    private UpdatingGroup progressivePopular;
    private ItemCardList itemListTrending;
    private ItemCardList itemListPopular;
    private Disposable disposable;
    private OnItemClickListenerDetails onItemClickListener = new OnItemClickListenerDetails(new RouterProvider() {
        @Override
        public Router get() {
            return getRouter();
        }
    });
    private UpdatingGroup progressiveUpcoming;
    private ItemListHorizontal itemListUpcoming;


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);

        GroupAdapter adapterMain = new GroupAdapter();
        GroupAdapter adapterUpcoming = new GroupAdapter();
        GroupAdapter adapterTrending = new GroupAdapter();
        GroupAdapter adapterPopular = new GroupAdapter();
        progressiveUpcoming = new UpdatingGroup();
        progressiveTrending = new UpdatingGroup();
        progressivePopular = new UpdatingGroup();
        itemListTrending = new ItemCardList(getActivity(), adapterTrending, "trending", presenter.onLoadMoreObserverTrending(), onItemClickListener);
        itemListPopular = new ItemCardList(getActivity(), adapterPopular, "popular", presenter.onLoadMoreObserverPoplar(), onItemClickListener);

        itemListUpcoming = new ItemListHorizontalPager(getActivity(), adapterUpcoming, "UPCOMING", onItemClickListener);

        adapterUpcoming.add(progressiveUpcoming);
        Section sectionUpcoming = new Section(new ItemCardHeader(1, "UPCOMING", "See All", null));
        sectionUpcoming.add(itemListUpcoming);

        final PublishSubject<Object> onClickListener = PublishSubject.create();
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
        setToolbar(binding.toolbar);

        int pageSize = getResources().getInteger(R.integer.page_size);


        presenter.updateStream(pageSize).subscribe(this);

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
        progressiveTrending.update(state.trending());
        progressivePopular.update(state.popular());
        itemListTrending.loading(state.loadingTrending());
        itemListTrending.itemLoadedCount(state.itemLoadingCountTrending());
        itemListTrending.page(state.pageTrending());
        itemListPopular.loading(state.loadingPopular());
        itemListPopular.itemLoadedCount(state.itemLoadingCountPopular());
        itemListPopular.page(state.pagePopular());


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
        Log.wtf(TAG, "onComplete() called x_X");
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        dispose(disposable);
        super.onDestroyView(view);
    }

    /*public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }*/

}
