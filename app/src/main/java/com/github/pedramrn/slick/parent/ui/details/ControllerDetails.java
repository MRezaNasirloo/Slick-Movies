package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerDetailsBinding;
import com.github.pedramrn.slick.parent.ui.BottomNavigationHandlerImpl;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;
import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdropList;
import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdropProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCast;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastList;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemHeader;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.main.BottomBarHost;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.UpdatingGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-28
 */

public class ControllerDetails extends ControllerBase implements ViewDetails, Observer<ViewStateDetails> {

    @Inject
    Provider<PresenterDetails> provider;
    @Presenter
    PresenterDetails presenter;

    @Inject
    BottomNavigationHandlerImpl bottomNavigationHandler;

    private String transitionName;
    private Movie movie;
    private CompositeDisposable disposable;
    private UpdatingGroup progressiveCast;
    private UpdatingGroup progressiveBackdrop;
    private GroupAdapter adapterMain;
    private GroupAdapter adapterCasts;
    private GroupAdapter adapterBackdrop;
    private List<ItemBackdropProgressive> progressiveBackdropList = new ArrayList<>(5);
    private List<ItemCastProgressive> progressiveCastList = new ArrayList<>(5);
    private ControllerChangeHandler changeHandler;

    public ControllerDetails(@NonNull Movie movie, String transitionName) {
        this(new BundleBuilder(new Bundle())
                .putParcelable("ITEM", movie)
                .putString("TRANSITION_NAME", transitionName)
                .build());
    }

    public ControllerDetails(@Nullable Bundle args) {
        super(args);
        transitionName = getArgs().getString("TRANSITION_NAME");
        movie = getArgs().getParcelable("ITEM");
    }

    private static final String TAG = ControllerDetails.class.getSimpleName();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        Slick.bind(this);
        ControllerDetailsBinding binding = ControllerDetailsBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            ((ToolbarHost) getActivity()).setToolbar(binding.toolbar).setupButton(true);
        }
        disposable = new CompositeDisposable();
        Context context = getApplicationContext();
        binding.imageViewHeader.loadNoFade(movie.posterThumbnail());

        // binding.toolbar.setTitle(movie.name());
        binding.collapsingToolbar.setTitle(movie.title());


        adapterMain = new GroupAdapter();
        adapterCasts = new GroupAdapter();
        adapterBackdrop = new GroupAdapter();

        ItemCastList itemCastList = new ItemCastList(adapterCasts);
        ItemBackdropList itemBackdropList = new ItemBackdropList(adapterBackdrop);
        adapterMain.add(new ItemHeader(movie, transitionName));//Summery from omdb
        Log.d(TAG, "onCreateView() called");
        adapterMain.add(itemCastList);//Casts from tmdb
        adapterMain.add(itemBackdropList);//Backdrops from tmdb

        if (progressiveCast == null) {
            progressiveBackdropList = new ArrayList<>(5);
            progressiveBackdropList = new ArrayList<>(5);
            this.progressiveCast = new UpdatingGroup();
            this.progressiveBackdrop = new UpdatingGroup();
            for (int i = 0; i < 5; i++) {
                progressiveCastList.add(new ItemCastProgressive(i));
                progressiveBackdropList.add(new ItemBackdropProgressive(i));
            }
        }
        adapterCasts.add(progressiveCast);
        adapterBackdrop.add(progressiveBackdrop);
        progressiveCast.update(progressiveCastList);
        progressiveBackdrop.update(progressiveBackdropList);

        binding.recyclerViewDetails.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewDetails.addItemDecoration(
                new ItemDecorationMargin(getResources().getDimensionPixelSize(R.dimen.item_decoration_margin)));
        binding.recyclerViewDetails.setAdapter(adapterMain);

        disposable.add(bottomNavigationHandler.handle((BottomBarHost) getParentController(), binding.recyclerViewDetails));

        presenter.updateStream().subscribe(this);
        presenter.getMovieDetails(movie.id());

        adapterCasts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                // getRouter().pushController(RouterTransaction.with(ProfileController((itemCasts.movieFull(item.getPosition(item))))));
                Toast.makeText(ControllerDetails.this.getActivity(),
                        String.format(Locale.ENGLISH, "You clicked %s", ((ItemCast) item).getCast().name()), Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        if (disposable != null) disposable.dispose();
    }

    @Override
    public void render(ViewStateDetails state) {
        final Movie movie = state.movieDetails();
        adapterMain.add(new ItemOverview(movie.overview()));
        adapterMain.add(new ItemOverview(movie.overview()));
        adapterMain.add(new ItemOverview(movie.overview()));
        adapterMain.add(new ItemOverview(movie.overview()));
        adapterMain.add(new ItemOverview(movie.overview()));
        adapterMain.add(new ItemOverview(movie.overview()));
        adapterMain.add(new ItemOverview(movie.overview()));
        progressiveCast.update(state.itemCasts());
        progressiveBackdrop.update(state.itemBackdrops());
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable.add(d);
    }

    @Override
    public void onNext(ViewStateDetails state) {
        render(state);
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onChangeStarted(@NonNull ControllerChangeHandler changeHandler, @NonNull ControllerChangeType changeType) {
        this.changeHandler = changeHandler;
        Log.d(TAG, "onChangeStarted() called");
    }
}
