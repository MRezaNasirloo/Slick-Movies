package com.github.pedramrn.slick.parent.ui.videos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerVideosBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.videos.state.ViewStateVideos;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.UpdatingGroup;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerVideos extends ControllerBase implements ViewVideos, Observer<ViewStateVideos> {

    @Inject
    Provider<PresenterVideos> provider;
    @Presenter
    PresenterVideos presenter;

    private final String transitionName;
    private final MovieBasic movie;
    private Disposable disposable;
    private UpdatingGroup adapterProgressive;

    public ControllerVideos(@NonNull MovieBasic movie, String transitionName) {
        this(new BundleBuilder(new Bundle())
                .putParcelable("ITEM", movie)
                .putString("TRANSITION_NAME", transitionName)
                .build());
    }

    public ControllerVideos(@Nullable Bundle args) {
        super(args);
        transitionName = getArgs().getString("TRANSITION_NAME");
        movie = getArgs().getParcelable("ITEM");
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        // TODO: 2017-07-22 Inject dependencies
        App.componentMain().inject(this);
        ControllerVideos_Slick.bind(this);
        ControllerVideosBinding binding = ControllerVideosBinding.inflate(inflater, container, false);
        setToolbar(binding.toolbar).setupButton(true);
        GroupAdapter adapter = new GroupAdapter();
        adapterProgressive = new UpdatingGroup();
        adapter.add(adapterProgressive);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.addItemDecoration(new ItemDecorationMargin(getResources().getDimensionPixelSize(R.dimen.item_decoration_margin)));
        binding.recyclerView.getItemAnimator().setChangeDuration(0);
        binding.recyclerView.setAdapter(adapter);
        presenter.updateStream().subscribe(this);
        presenter.get(movie.id());
        return binding.getRoot();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        dispose(disposable);
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(ViewStateVideos state) {
        adapterProgressive.update(state.videos());
        renderError(state.errorVideos());
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
