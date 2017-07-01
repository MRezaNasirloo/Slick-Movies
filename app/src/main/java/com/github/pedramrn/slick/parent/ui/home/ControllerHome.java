package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerHomeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.home.item.ItemAnticipatedList;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

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
    GroupAdapter adapterMain = new GroupAdapter();
    GroupAdapter adapterAnticipated = new GroupAdapter();
    UpdatingGroup progressiveAnticipated = new UpdatingGroup();
    ItemAnticipatedList itemAnticipatedList = new ItemAnticipatedList(adapterAnticipated);
    private Disposable disposable;
    private UpdatingGroup adapterTrending;


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        Slick.bind(this);
        ControllerHomeBinding binding = ControllerHomeBinding.inflate(inflater, container, false);


        if (adapterMain.getAdapterPosition(itemAnticipatedList) == -1) {
            adapterMain.add(itemAnticipatedList);
            adapterAnticipated.add(progressiveAnticipated);

            adapterTrending = new UpdatingGroup();
            Section sectionTrending = new Section(adapterTrending);
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
        Log.d(TAG, "render() called on " + Thread.currentThread().getName());
        progressiveAnticipated.update(state.anticipated());
        renderError(state.videosError());

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
