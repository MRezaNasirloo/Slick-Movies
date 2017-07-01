package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.home.item.ItemAnticipatedList;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

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
    private UpdatingGroup progressiveAnticipated = new UpdatingGroup();
    private UpdatingGroup progressiveTrending = new UpdatingGroup();
    private ItemCardList itemCardList = new ItemCardList(adapterTrending);
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
            sectionTrending.add(itemCardList);
            adapterMain.add(sectionTrending);

            adapterMain.add(new ItemOverview("Nunquam carpseris candidatus.Hercle, domus neuter!, palus!"));
        }

        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewHome.setAdapter(adapterMain);
        setToolbar(binding.toolbar);

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    private static final String TAG = ControllerHome.class.getSimpleName();
    @Override
    public void render(@NonNull ViewStateHome state) {
        ViewStateHomeRenderer renderer = new ViewStateHomeRenderer(state);
        Log.d(TAG, "render() called on " + Thread.currentThread().getName());
        List<Item> trending = renderer.trending();
        progressiveAnticipated.update(renderer.anticipated());
        progressiveTrending.update(trending);

        // TODO: 2017-07-01 extract this logic
        Item item = progressiveTrending.getItem(0);
        if (item != null && trending.size() > 0 && item.getId() != trending.get(0).getId()) {
            progressiveTrending.remove(item);
            progressiveTrending.remove(progressiveTrending.getItem(1));
            progressiveTrending.remove(progressiveTrending.getItem(2));
        }
        // TODO: 2017-07-01 You're better than this...
        renderError(state.videosError());
        renderError(state.trendingError());

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
        super.onDestroyView(view);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
