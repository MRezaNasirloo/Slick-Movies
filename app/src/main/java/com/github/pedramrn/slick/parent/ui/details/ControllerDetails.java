package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.github.pedramrn.slick.parent.ui.custom.ImageViewLoader;
import com.github.pedramrn.slick.parent.ui.details.item.ItemComment;
import com.github.pedramrn.slick.parent.ui.details.item.ItemHeader;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Comment;
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
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

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

    // @Inject
    // List<MovieTmdb> movieTmdbs;

    private MovieBasic movie;
    private String transitionName;

    private UpdatingGroup progressiveBackdrop;
    private UpdatingGroup progressiveComments;
    private UpdatingGroup progressiveSimilar;
    private UpdatingGroup progressiveCast;
    private UpdatingGroup updatingHeader;

//    private ItemListHorizontal itemHeader;
    private ItemListHorizontal itemBackdropList;
    private ItemCardList itemCardListSimilar;

    private Section sectionOverview;

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageViewLoader imageViewHeader;

    private ViewStateDetails state;
    private GroupAdapter adapterMain;
    private ItemCardHeader headerComments;
    private ItemCardHeader headerCast;
    private ItemHeader headerMovie;
    private PublishSubject<String> onRetry = PublishSubject.create();

    public ControllerDetails(@NonNull MovieBasic movie, String transitionName) {
        this(new BundleBuilder(new Bundle())
                .putParcelable("ITEM", movie)
                .putString("TRANSITION_NAME", transitionName)
                .build());
    }

    @SuppressWarnings("WeakerAccess")
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
        ControllerDetailsBinding binding = ControllerDetailsBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            ((ToolbarHost) getActivity()).setToolbar(binding.toolbar).setupButton(binding.toolbar, true);
        }

        collapsingToolbar = binding.collapsingToolbar;
        imageViewHeader = binding.imageViewHeader;

        final Context context = getApplicationContext();

        adapterMain = new GroupAdapter();
        GroupAdapter adapterSimilar = new GroupAdapter();
        GroupAdapter adapterBackdrops = new GroupAdapter();

        setOnItemClickListener(adapterMain);
        setOnItemClickListener(adapterSimilar);
        setOnItemClickListener(adapterBackdrops);

        updatingHeader = new UpdatingGroup();
        progressiveCast = new UpdatingGroup();
        progressiveSimilar = new UpdatingGroup();
        progressiveBackdrop = new UpdatingGroup();
        progressiveComments = new UpdatingGroup();

        adapterMain.setSpanCount(6);

        Section header = new Section(updatingHeader);
        updatingHeader.update(Collections.singletonList(new ItemHeader(this, movie, transitionName)));

        itemCardListSimilar = new ItemCardList(context, adapterSimilar, "SIMILAR");
        adapterSimilar.add(progressiveSimilar);

        Section sectionSimilar = new Section(new ItemCardHeader(100, "Similar"));
        sectionSimilar.add(itemCardListSimilar);

        headerCast = new ItemCardHeader(101, "Casts", "See All");
        headerCast.setOnClickListener(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (state.movieBasic() instanceof Movie && !((Movie) state.movieBasic()).casts().isEmpty()) {
                    ArrayList<Cast> casts = (ArrayList<Cast>) ((Movie) state.movieBasic()).casts();
                    ControllerList.start(getRouter(), state.movieBasic().title() + "'s Casts",
                                         new ArrayList<ItemViewListParcelable>(casts));
                }
            }
        });
        Section sectionCasts = new Section(headerCast);
        sectionCasts.add(progressiveCast);

        headerComments = new ItemCardHeader(102, "Comments", "See All");
        headerComments.setOnClickListener(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (!state.comments().isEmpty() && state.comments().get(0) instanceof ItemComment) {
                    ControllerList.start(ControllerDetails.this.getRouter(), "Comments for " + movie.title(), comments());
                }
            }
        });
        Section sectionComments = new Section(headerComments);
        sectionComments.add(progressiveComments);

        itemBackdropList = new ItemListHorizontal(adapterBackdrops, "BACKDROPS");
        adapterBackdrops.add(progressiveBackdrop);

        Section sectionBackdrops = new Section(new ItemCardHeader(103, "Backdrops"));
        sectionBackdrops.add(itemBackdropList);

        sectionOverview = new Section(new ItemCardHeader(104, "Overview"));


        adapterMain.add(header);
        adapterMain.add(sectionCasts);
        adapterMain.add(sectionOverview);
        adapterMain.add(sectionBackdrops);
        adapterMain.add(sectionComments);
        adapterMain.add(sectionSimilar);

        GridLayoutManager lm = new GridLayoutManager(context, adapterMain.getSpanCount(), LinearLayoutManager.VERTICAL, false);
        lm.setSpanSizeLookup(adapterMain.getSpanSizeLookup());
        binding.recyclerViewDetails.setLayoutManager(lm);
        binding.recyclerViewDetails.setAdapter(adapterMain);
        binding.recyclerViewDetails.getItemAnimator().setChangeDuration(0);
        binding.recyclerViewDetails.getItemAnimator().setMoveDuration(0);

        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = Observable.fromIterable(movieTmdbs)
                        .map(new MapperMovie(new MapperCast(), new MapperSimpleData()))
                        .map(new MapperMovieDomainMovie())
                        .blockingFirst();
                ((OnItemAction) movie.render("")).action(ControllerDetails.this, null, 0);
            }
        });*/

//        add(bottomNavigationHandler.handle((BottomBarHost) getParentController(), binding.recyclerViewDetails));

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    @Override
    public void render(ViewStateDetails state) {
        this.state = state;
        long before = System.currentTimeMillis();

        movie = state.movieBasic();

        collapsingToolbar.setTitle(movie.title());
        imageViewHeader.loadBlur(movie.thumbnailBackdrop());

        updatingHeader.update(Collections.singletonList(new ItemHeader(this, movie, transitionName)));

        if (sectionOverview.getGroup(1) == null && movie.overview() != null) {
            sectionOverview.add(new ItemOverview(movie.overview()));
        }

        progressiveCast.update(state.casts());
        progressiveBackdrop.update(state.backdrops());
        progressiveSimilar.update(state.similar());

        List<Item> comments = Observable.fromIterable(state.comments())
                .take(2)
                .buffer(2)
                .blockingFirst(Collections.<Item>emptyList());
        //only show the first 2 items
        progressiveComments.update(comments);

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
        Log.d(TAG, "onDestroyView");
        adapterMain.clear();
        itemCardListSimilar.onDestroyView();
        itemBackdropList.onDestroyView();
//        itemHeader.onDestroyView();
        headerCast.onDestroyView();
        headerComments.onDestroyView();
//        headerMovie.onDestroyView();

        updatingHeader = null;
        progressiveCast = null;
        progressiveSimilar = null;
        progressiveBackdrop = null;
        progressiveComments = null;

        itemCardListSimilar = null;
        itemBackdropList = null;
        sectionOverview = null;

        collapsingToolbar = null;
        imageViewHeader = null;

        headerComments = null;
        headerMovie = null;
        headerCast = null;
//        itemHeader = null;

        adapterMain = null;
        super.onDestroyView(view);
    }

    @Override
    public MovieBasic get() {
        return movie;
    }

    @NonNull
    protected ArrayList<ItemViewListParcelable> comments() {
        return (ArrayList<ItemViewListParcelable>) Observable.fromIterable(state.comments())
                .cast(ItemComment.class)
                .map(new Function<ItemComment, Comment>() {
                    @Override
                    public Comment apply(@NonNull ItemComment itemComment) throws Exception {
                        return itemComment.comment();
                    }
                })
                .cast(ItemViewListParcelable.class)
                .buffer(state.comments().size())
                .blockingFirst(Collections.<ItemViewListParcelable>emptyList());
    }

    private void setOnItemClickListener(final GroupAdapter adapter) {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Item item, View view) {
                ((OnItemAction) item).action(ControllerDetails.this, null, adapter.getAdapterPosition(item));
            }
        });
    }

    @Override
    public Observable<Object> onRetryComments(){
        return onRetry.filter(new Predicate<String>() {
            @Override
            public boolean test(@NonNull String tag) throws Exception {
                return "COMMENTS_ERROR".equals(tag);
            }
        }).cast(Object.class);
    }

    @Override
    public void showMainError() {
    }

    @Override
    public void showMainContents() {

    }

    @Override
    public void onRetry(String tag) {
        onRetry.onNext(tag);
    }
}
