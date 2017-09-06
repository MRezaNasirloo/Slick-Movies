package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerDetailsBinding;
import com.github.pedramrn.slick.parent.ui.BottomNavigationHandlerImpl;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCast;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemHeader;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.ControllerProvider;
import com.github.pedramrn.slick.parent.ui.home.OnItemClickListenerAction;
import com.github.pedramrn.slick.parent.ui.home.RouterProvider;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.github.pedramrn.slick.parent.ui.list.ControllerList;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.main.BottomBarHost;
import com.github.slick.Presenter;
import com.github.slick.Slick;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    private UpdatingGroup progressiveComments;
    private UpdatingGroup progressiveSimilar;
    private UpdatingGroup updatingHeader;
    private ControllerDetailsBinding binding;
    private CompositeDisposable disposable;
    private ItemCardList itemCardListSimilar;
    private ItemListHorizontal itemBackdropList;
    private Section sectionOverview;

    private final RouterProvider routerProvider = new RouterProvider() {
        @Override
        public Router get() {
            return getRouter();
        }
    };
    private final OnItemClickListenerAction onItemClickListener = new OnItemClickListenerAction(
            routerProvider, new ControllerProvider() {
        @Override
        public Controller get(MovieBasic movie, String transitionName) {
            return new ControllerDetails(movie, transitionName);
        }
    });
    private ViewStateDetails state;

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
        final Context context = getApplicationContext();

        GroupAdapter adapterMain = new GroupAdapter();
        GroupAdapter adapterHeader = new GroupAdapter();
        GroupAdapter adapterBackdrops = new GroupAdapter();
        GroupAdapter adapterSimilar = new GroupAdapter();
        updatingHeader = new UpdatingGroup();
        progressiveCast = new UpdatingGroup();
        progressiveSimilar = new UpdatingGroup();
        progressiveBackdrop = new UpdatingGroup();
        progressiveComments = new UpdatingGroup();

        adapterMain.setSpanCount(6);

        ItemListHorizontal itemHeader = new ItemListHorizontal(context, adapterHeader, "HEADER", null);
        adapterHeader.add(updatingHeader);

        itemCardListSimilar = new ItemCardList(context, adapterSimilar, "SIMILAR", PublishSubject.<Integer>create(), onItemClickListener);
        Consumer<Object> onClickListener = new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Toast.makeText(context, "Under Construction", Toast.LENGTH_SHORT).show();
            }
        };
        Section sectionSimilar = new Section(new ItemCardHeader(0, "Similar"));
        sectionSimilar.add(itemCardListSimilar);
        adapterSimilar.add(progressiveSimilar);

        Section sectionCasts = new Section(new ItemCardHeader(0, "Casts", "See All", new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Log.e(TAG, "See All Casts called");
                if (state.movieBasic() instanceof Movie && !((Movie) state.movieBasic()).casts().isEmpty()) {
                    getRouter().pushController(RouterTransaction.with(new ControllerList(state.movieBasic().title() + "'s Casts",
                            ((Movie) state.movieBasic()).casts().toArray(new ItemViewListParcelable[state.casts().size()])))
                            .popChangeHandler(new HorizontalChangeHandler())
                            .pushChangeHandler(new HorizontalChangeHandler())
                    );
                }
            }
        }));
        sectionCasts.add(progressiveCast);
        adapterMain.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                if (item instanceof OnItemAction) {
                    ((OnItemAction) item).action(routerProvider);
                }
            }
        });

        Section sectionComments = new Section(new ItemCardHeader(0, "Comments", "See All", onClickListener));
        sectionComments.add(progressiveComments);
        sectionComments.setPlaceholder(new ItemCastProgressive(-1));
        sectionComments.setHideWhenEmpty(true);

        itemBackdropList = new ItemListHorizontal(context, adapterBackdrops, "BACKDROPS", onItemClickListener);
        Section sectionBackdrops = new Section(new ItemCardHeader(0, "Backdrops"));
        sectionBackdrops.add(itemBackdropList);
        adapterBackdrops.add(progressiveBackdrop);

        sectionOverview = new Section(new ItemCardHeader(0, "Overview"));

        adapterMain.add(itemHeader);
        adapterMain.add(sectionCasts);
        adapterMain.add(sectionOverview);
        adapterMain.add(sectionBackdrops);
        adapterMain.add(sectionComments);
        adapterMain.add(sectionSimilar);

        GridLayoutManager layoutManager = new GridLayoutManager(context, adapterMain.getSpanCount(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(adapterMain.getSpanSizeLookup());
        binding.recyclerViewDetails.setLayoutManager(layoutManager);
        binding.recyclerViewDetails.setAdapter(adapterMain);
        binding.recyclerViewDetails.getItemAnimator().setChangeDuration(0);
        binding.recyclerViewDetails.getItemAnimator().setMoveDuration(0);

        disposable.add(bottomNavigationHandler.handle((BottomBarHost) getParentController(), binding.recyclerViewDetails));

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

        final MovieBasic movie = state.movieBasic();

        binding.collapsingToolbar.setTitle(movie.title());
        binding.imageViewHeader.loadBlur(movie.thumbnailBackdrop());

        updatingHeader.update(Collections.singletonList(new ItemHeader(movie, transitionName)));

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
        dispose(disposable);
        binding.unbind();
        super.onDestroyView(view);
    }

    @Override
    protected void onDestroy() {
        dispose(disposable);
        super.onDestroy();
    }
}
