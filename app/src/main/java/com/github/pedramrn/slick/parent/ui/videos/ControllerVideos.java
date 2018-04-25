package com.github.pedramrn.slick.parent.ui.videos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerVideosBinding;
import com.github.pedramrn.slick.parent.exception.NotImplementedException;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.SnackbarManager;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.ErrorHandlerSnackbar;
import com.github.pedramrn.slick.parent.ui.details.ItemDecorationMargin;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.mrezanasirloo.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.UpdatingGroup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerVideos extends ControllerBase implements ViewVideos, OnItemClickListener {

    @Inject
    Provider<PresenterVideos> provider;
    @Presenter
    PresenterVideos presenter;

    private final String transitionName;
    private final MovieBasic movie;
    private UpdatingGroup adapterProgressive;
    private ErrorHandlerSnackbar snackbar;

    private PublishSubject<Object> errorDismissed = PublishSubject.create();
    private PublishSubject<Object> retry = PublishSubject.create();
    private GroupAdapter adapter;

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

    ControllerVideosBinding binding;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        PresenterVideos_Slick.bind(this);
        binding = ControllerVideosBinding.inflate(inflater, container, false);
        setToolbar(binding.toolbar).setupButton(binding.toolbar, true);
        binding.toolbar.setTitle(String.format("Videos: %s", movie.title()));
        adapter = new GroupAdapter();
        adapterProgressive = new UpdatingGroup();
        adapter.add(adapterProgressive);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,
                false));
        binding.recyclerView.addItemDecoration(new ItemDecorationMargin(getResources().getDimensionPixelSize(R.dimen
                .item_decoration_margin)));
        binding.recyclerView.getItemAnimator().setChangeDuration(0);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        return binding.getRoot();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        snackbar = new ErrorHandlerSnackbar(view, "Retry?") {

            @Override
            public void onDismissed(int event) {
                errorDismissed.onNext(1);
                if (Snackbar.Callback.DISMISS_EVENT_ACTION == event) {
                    // errorDismissed.onNext(1);
                    retry.onNext(1);
                }
            }

            /*@Override
            public void onAction() {
                *//*errorDismissed.onNext(1);
                retry.onNext(1);*//*
            }*/
        };
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        binding.recyclerView.setAdapter(null);
        adapter.setOnItemClickListener(null);
        adapterProgressive = null;
        adapter = null;
        binding = null;
        snackbar = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private static final String TAG = ControllerVideos.class.getSimpleName();

    public static void start(Router router, MovieBasic movie, String transitionName, int position) {
        router.pushController(RouterTransaction.with(new ControllerVideos(movie, transitionName))
                .pushChangeHandler(new HorizontalChangeHandler())
                .popChangeHandler(new HorizontalChangeHandler()));
    }

    @Override
    public MovieBasic movie() {
        return movie;
    }

    @Override
    public void update(List<Item> videos) {
        adapterProgressive.update(videos);
    }

    @Override
    public void error(short code) {
        snackbar.show(ErrorHandler.message(getApplicationContext(), code));
    }

    @Override
    public Observable<Object> onErrorDismissed() {
        return errorDismissed;
    }

    @Override
    public Observable<Object> onRetry() {
        return retry;
    }

    @Override
    public void onItemClick(Item item, View view) {
        ((OnItemAction) item).action(ControllerVideos.this, null, adapter.getAdapterPosition(item));
    }

    @NonNull
    @Override
    public SnackbarManager snackbarManager() {
        throw new NotImplementedException("Sorry!!!");
    }
}
