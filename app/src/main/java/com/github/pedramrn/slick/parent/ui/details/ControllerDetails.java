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
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.main.BottomBarHost;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

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

    private MovieBasic movie;
    private String transitionName;
    private UpdatingGroup progressiveCast;
    private UpdatingGroup progressiveBackdrop;
    private UpdatingGroup progressiveSimilar;
    private UpdatingGroup updatingHeader;
    private ControllerDetailsBinding binding;
    private CompositeDisposable disposable;

    public ControllerDetails(@NonNull MovieBasic movie, String transitionName) {
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
        binding = ControllerDetailsBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            ((ToolbarHost) getActivity()).setToolbar(binding.toolbar).setupButton(true);
        }
        disposable = new CompositeDisposable();
        Context context = getApplicationContext();

        GroupAdapter adapterMain = new GroupAdapter();
        GroupAdapter adapterCasts = new GroupAdapter();
        GroupAdapter adapterBackdrops = new GroupAdapter();
        GroupAdapter adapterSimilar = new GroupAdapter();
        updatingHeader = new UpdatingGroup();
        progressiveCast = new UpdatingGroup();
        progressiveSimilar = new UpdatingGroup();
        progressiveBackdrop = new UpdatingGroup();

        ItemCardList itemCardListSimilar = new ItemCardList(adapterSimilar, "SIMILAR", PublishSubject.<Integer>create());
        Section sectionSimilar = new Section(new ItemCardHeader(0, "Similar", "See All", PublishSubject.create()));
        sectionSimilar.add(itemCardListSimilar);
        adapterSimilar.add(progressiveSimilar);

        ItemCastList itemCastList = new ItemCastList(adapterCasts);
        Section sectionCasts = new Section(new ItemCardHeader(0, "Casts", null, PublishSubject.create()));
        sectionCasts.add(itemCastList);
        adapterCasts.add(progressiveCast);

        ItemBackdropList itemBackdropList = new ItemBackdropList(adapterBackdrops);
        Section sectionBackdrops = new Section(new ItemCardHeader(0, "Backdrops", null, PublishSubject.create()));
        sectionBackdrops.add(itemBackdropList);
        adapterBackdrops.add(progressiveBackdrop);

        adapterMain.add(updatingHeader);
        adapterMain.add(sectionCasts);
        adapterMain.add(sectionBackdrops);
        adapterMain.add(sectionSimilar);

        binding.recyclerViewDetails.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewDetails.addItemDecoration(new ItemDecorationMargin(getResources().getDimensionPixelSize(R.dimen.item_decoration_margin)));
        binding.recyclerViewDetails.setAdapter(adapterMain);
        binding.recyclerViewDetails.getItemAnimator().setChangeDuration(0);

        disposable.add(bottomNavigationHandler.handle((BottomBarHost) getParentController(), binding.recyclerViewDetails));

        presenter.updateStream().subscribe(this);
        presenter.getMovieDetails(movie);

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
        dispose(disposable);
    }

    @Override
    public void render(ViewStateDetails state) {
        Log.d(TAG, "render() called with: state = [" + state + "]");
        final MovieBasic movie = state.movieBasic();
        if (movie.voteAverageTrakt() != null) {
            updatingHeader.update(Collections.singletonList(new ItemHeader(movie, transitionName)));
        }
        /*// FIXME: 2017-07-14 I'm adding myself every time :)
        adapterMain.add(new ItemOverview(movie.overview()));*/

        binding.imageViewHeader.loadNoFade(movie.posterThumbnail());
        binding.collapsingToolbar.setTitle(movie.title());
        updatingHeader.update(Collections.singletonList(new ItemHeader(movie, transitionName)));//Summery from tmdb


        progressiveCast.update(state.casts());
        progressiveBackdrop.update(state.backdrops());
        progressiveSimilar.update(state.similar());

        renderError(state.errorSimilar());
        renderError(state.errorMovie());
        renderError(state.errorMovieBackdrop());
        renderError(state.errorMovieCast());
    }

    @Override
    public void onSubscribe(Disposable d) {
        dispose(disposable);
        disposable = new CompositeDisposable(d);
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
        dispose(disposable);
        super.onDestroy();
    }
}
