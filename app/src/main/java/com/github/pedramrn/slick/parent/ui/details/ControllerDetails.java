package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerDetailsBinding;
import com.github.pedramrn.slick.parent.ui.BottomNavigationHandlerImpl;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCast;
import com.github.pedramrn.slick.parent.ui.details.item.ItemHeader;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.MovieProvider;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.github.pedramrn.slick.parent.ui.list.ControllerList;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.slick.Presenter;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-28
 */

public class ControllerDetails extends ControllerElm<ViewStateDetails> implements ViewDetails, MovieProvider {

    private static final String TAG = ControllerDetails.class.getSimpleName();

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
    private UpdatingGroup progressiveComments;
    private UpdatingGroup progressiveSimilar;
    private UpdatingGroup updatingHeader;
    private ControllerDetailsBinding binding;
    private CompositeDisposable disposable;
    private ItemCardList itemCardListSimilar;
    private ItemListHorizontal itemBackdropList;
    private Section sectionOverview;
    private ViewStateDetails state;
    private ItemCardHeader headerCast;
    private ItemCardHeader headerComments;
    private ItemHeader headerMovie;
    private GroupAdapter adapterMain;

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

    public static void start(@NonNull Router router, @NonNull MovieBasic movie, String transitionName) {
        router.pushController(RouterTransaction.with(new ControllerDetails(movie, transitionName))
                                      .pushChangeHandler(new HorizontalChangeHandler())
                                      .popChangeHandler(new HorizontalChangeHandler())
        );
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        ControllerDetails_Slick.bind(this);
        binding = ControllerDetailsBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            ((ToolbarHost) getActivity()).setToolbar(binding.toolbar).setupButton(binding.toolbar, true);
        }
        final Context context = getApplicationContext();

        adapterMain = new GroupAdapter();
        GroupAdapter adapterHeader = new GroupAdapter();
        GroupAdapter adapterBackdrops = new GroupAdapter();
        GroupAdapter adapterSimilar = new GroupAdapter();
        updatingHeader = new UpdatingGroup();
        progressiveCast = new UpdatingGroup();
        progressiveSimilar = new UpdatingGroup();
        progressiveBackdrop = new UpdatingGroup();
        progressiveComments = new UpdatingGroup();

        adapterMain.setSpanCount(6);

        ItemListHorizontal itemHeader = new ItemListHorizontal(adapterHeader, "HEADER");
        adapterHeader.add(updatingHeader);

        itemCardListSimilar = new ItemCardList(context, adapterSimilar, "SIMILAR");
//        adapterSimilar.setOnItemClickListener(this);
        adapterSimilar.add(progressiveSimilar);

        Section sectionSimilar = new Section(new ItemCardHeader(0, "Similar"));
        sectionSimilar.add(itemCardListSimilar);

        headerCast = new ItemCardHeader(0, "Casts", "See All");
        headerCast.setOnClickListener(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (state.movieBasic() instanceof Movie && !((Movie) state.movieBasic()).casts().isEmpty()) {
                    ControllerList.start(getRouter(), state.movieBasic().title() + "'s Casts",
                                         ((Movie) state.movieBasic()).casts()
                                                 .toArray(new ItemViewListParcelable[state.casts().size()])
                    );
                }
            }
        });
        Section sectionCasts = new Section(headerCast);
        sectionCasts.add(progressiveCast);

        headerComments = new ItemCardHeader(0, "Comments", "See All");
        headerComments.setOnClickListener(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Snackbar.make(getView(), "Comming soon :)", Snackbar.LENGTH_LONG).show();
            }
        });
        Section sectionComments = new Section(headerComments);
        sectionComments.add(progressiveComments);
        sectionComments.setHideWhenEmpty(true);

        itemBackdropList = new ItemListHorizontal(adapterBackdrops, "BACKDROPS");
//        adapterBackdrops.setOnItemClickListener(this);
        adapterBackdrops.add(progressiveBackdrop);


        Section sectionBackdrops = new Section(new ItemCardHeader(0, "Backdrops"));
        sectionBackdrops.add(itemBackdropList);

        sectionOverview = new Section(new ItemCardHeader(0, "Overview"));

        adapterMain.add(itemHeader);
        adapterMain.add(sectionCasts);
        adapterMain.add(sectionOverview);
        adapterMain.add(sectionBackdrops);
        adapterMain.add(sectionComments);
        adapterMain.add(sectionSimilar);
        //        adapterMain.setOnItemClickListener(this);


        GridLayoutManager lm = new GridLayoutManager(context, adapterMain.getSpanCount(), LinearLayoutManager.VERTICAL, false);
        lm.setSpanSizeLookup(adapterMain.getSpanSizeLookup());
        binding.recyclerViewDetails.setLayoutManager(lm);
        binding.recyclerViewDetails.setAdapter(adapterMain);
        binding.recyclerViewDetails.getItemAnimator().setChangeDuration(0);
        binding.recyclerViewDetails.getItemAnimator().setMoveDuration(0);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnItemAction) state.similar().get(0)).action(ControllerDetails.this, 0);
            }
        });

//        add(bottomNavigationHandler.handle((BottomBarHost) getParentController(), binding.recyclerViewDetails));

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    @NonNull
    protected ItemViewListParcelable[] casts() {
        return Observable.fromIterable(state.casts())
                .cast(ItemCast.class)
                .map(new Function<ItemCast, Cast>() {
                    @Override
                    public Cast apply(@io.reactivex.annotations.NonNull ItemCast itemCast) throws Exception {
                        return itemCast.cast();
                    }
                })
                .cast(ItemViewListParcelable.class)
                .buffer(state.casts().size())
                .blockingFirst()
                .toArray(new ItemViewListParcelable[state.casts().size()]);
    }

    @Override
    public void render(ViewStateDetails state) {
        this.state = state;
        long before = System.currentTimeMillis();

        movie = state.movieBasic();

        binding.collapsingToolbar.setTitle(movie.title());
        binding.imageViewHeader.loadBlur(movie.thumbnailBackdrop());

        headerMovie = new ItemHeader(null, movie, transitionName);
        updatingHeader.update(Collections.singletonList(headerMovie));

        if (sectionOverview.getGroup(1) == null && movie.overview() != null) {
            sectionOverview.add(new ItemOverview(movie.overview()));
        }

        progressiveCast.update(state.casts());
        progressiveBackdrop.update(state.backdrops());
        List<Item> comments = state.comments();
        progressiveComments.update(comments);
        progressiveSimilar.update(state.similar());

        Log.e(TAG, comments.toString());

        renderError(state.errorSimilar());
        renderError(state.errorComments());
        renderError(state.errorMovie());
        renderError(state.errorMovieBackdrop());
        renderError(state.errorMovieCast());

        long delay = System.currentTimeMillis() - before;
        int sizeCast = state.casts().size();
        int sizeBackdrops = state.backdrops().size();
        int sizeSimilar = state.similar().size();

        Log.e(TAG, String.format(Locale.ENGLISH, "casts: %d, backdrops: %d, similar: %d", sizeCast, sizeBackdrops, sizeSimilar));
        Log.e(TAG, String.format(Locale.ENGLISH, "Updating the list cost you %sms", delay));
    }

    @Override
    public MovieBasic getMovie() {
        return movie;
    }

    @Override
    public void onSubscribe(Disposable d) {
        add(d);
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
        Log.d(TAG, "onComplete() called");
    }


    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        itemBackdropList.onSaveViewState(view, outState);
        itemCardListSimilar.onSaveViewState(view, outState);
    }


    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        itemBackdropList.onRestoreViewState(view, savedViewState);
        itemCardListSimilar.onRestoreViewState(view, savedViewState);
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        binding.fab.setOnClickListener(null);
        headerCast.onDestroyView();
        headerCast.setOnClickListener(null);
        headerComments.onDestroyView();
        headerComments.setOnClickListener(null);
        headerMovie.onDestroyView();
        headerCast.setOnClickListener(null);
        adapterMain.setOnItemClickListener(null);
        adapterMain.setOnItemLongClickListener(null);
        adapterMain.clear();
        adapterMain = null;
        super.onDestroyView(view);
    }

    @Override
    public MovieBasic get() {
        return movie;
    }

}
