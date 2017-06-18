package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerDetailsBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;
import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;
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
import com.squareup.picasso.Picasso;
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

    private int pos;
    private MovieBoxOffice movieBoxOffice;
    private CompositeDisposable disposable;
    private UpdatingGroup progressiveCast;
    private UpdatingGroup progressiveBackdrop;
    private GroupAdapter adapterMain;
    private GroupAdapter adapterCasts;
    private GroupAdapter adapterBackdrop;
    private List<ItemBackdropProgressive> progressiveBackdropList = new ArrayList<>(5);
    private List<ItemCastProgressive> progressiveCastList = new ArrayList<>(5);

    public ControllerDetails(MovieBoxOffice movieBoxOffice, int position) {
        this(new BundleBuilder(new Bundle())
                .putParcelable("ITEM", movieBoxOffice)
                .putInt("POS", position)
                .build());
    }

    public ControllerDetails(@Nullable Bundle args) {
        super(args);
        pos = getArgs().getInt("POS");
        movieBoxOffice = getArgs().getParcelable("ITEM");
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
        Picasso.with(context).load(movieBoxOffice.posterMedium()).noFade().into(binding.imageViewHeader);

        // binding.toolbar.setTitle(movieBoxOffice.name());
        binding.collapsingToolbar.setTitle(movieBoxOffice.name());

        Disposable d = bottomNavigationHandler.handle((BottomBarHost) getParentController(), binding.recyclerViewDetails);

        disposable.add(d);

        adapterMain = new GroupAdapter();
        adapterCasts = new GroupAdapter();
        adapterBackdrop = new GroupAdapter();

        ItemCastList itemCastList = new ItemCastList(adapterCasts);
        ItemBackdropList itemBackdropList = new ItemBackdropList(adapterBackdrop);
        adapterMain.add(new ItemHeader(movieBoxOffice, pos));//Summery from omdb
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

        binding.recyclerViewDetails.setAdapter(adapterMain);
        binding.recyclerViewDetails.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        presenter.updateStream().subscribe(this);
        presenter.getMovieDetails(movieBoxOffice.tmdb());

        adapterCasts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                // getRouter().pushController(RouterTransaction.with(ProfileController((itemCasts.getFull(item.getPosition(item))))));
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
}
