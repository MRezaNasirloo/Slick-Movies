package com.github.pedramrn.slick.parent.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ControllerHome extends ControllerBase implements ViewHome, Observer<ViewStateHome> {

    @Inject
    Provider<PresenterHome> provider;
    @Presenter
    PresenterHome presenter;
    private GroupAdapter adapterMain = new GroupAdapter();
    private GroupAdapter adapterAnticipated = new GroupAdapter();
    private GroupAdapter adapterTrending = new GroupAdapter();
    private GroupAdapter adapterPopular = new GroupAdapter();
    private UpdatingGroup progressiveAnticipated = new UpdatingGroup();
    private UpdatingGroup progressiveTrending = new UpdatingGroup();
    private UpdatingGroup progressivePopular = new UpdatingGroup();
    private ItemCardList itemTrendingList = new ItemCardList(adapterTrending);
    // private ItemCardList itemPopularList = new ItemCardList(adapterPopular);
    private ItemAnticipatedList itemAnticipatedList = new ItemAnticipatedList(adapterAnticipated);
    private Disposable disposable;


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);


        if (adapterMain.getAdapterPosition(itemAnticipatedList) == -1) {
            adapterMain.add(itemAnticipatedList);
            adapterAnticipated.add(progressiveAnticipated);

            Section sectionTrending = new Section(new ItemCardHeader(1, "Trending", "See All", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ControllerHome.this.getActivity(), "Under Construction", Toast.LENGTH_SHORT).show();
                }
            }));
            adapterTrending.add(progressiveTrending);
            sectionTrending.add(itemTrendingList);

            Section sectionPopular = new Section(new ItemCardHeader(1, "Popular", "See All", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ControllerHome.this.getActivity(), "Under Construction", Toast.LENGTH_SHORT).show();
                }
            }));
            adapterPopular.add(progressivePopular);
            // sectionPopular.add(itemPopularList);

            adapterMain.add(sectionTrending);
            // adapterMain.add(sectionPopular);
        }

        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewHome.setAdapter(adapterMain);
        setToolbar(binding.toolbar);

        Observable<Integer> triggerTrending = itemTrendingList.getPageTrigger();
        // itemPopularList.getPageTrigger();

        int pageSize = getResources().getInteger(R.integer.page_size);
        presenter.updateStream(triggerTrending, triggerTrending, pageSize).subscribe(this);

        Log.e(TAG, "onCreateView() called");
        return binding.getRoot();
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        itemTrendingList.onSaveViewState(view, outState);
        // itemPopularList.onSaveViewState(view, outState);
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        itemTrendingList.onRestoreViewState(view, savedViewState);
        // itemPopularList.onRestoreViewState(view, savedViewState);
    }

    private static final String TAG = ControllerHome.class.getSimpleName();

    @Override
    public void render(@NonNull ViewStateHome state) {
        Log.d(TAG, "render() called");
        ViewStateHomeRenderer renderer = new ViewStateHomeRenderer(state);
        progressiveAnticipated.update(renderer.anticipated());
        progressiveTrending.update(renderer.trending());
        // progressivePopular.update(renderer.popular());


        // TODO: 2017-07-01 You're better than this...
        renderError(state.videosError());
        renderError(state.error());

    }

    private void renderError(@Nullable Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
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
        Log.d(TAG, "onDestroyView() called");
        super.onDestroyView(view);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
