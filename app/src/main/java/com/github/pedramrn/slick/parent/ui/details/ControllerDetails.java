package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
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
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerDetailsBinding;
import com.github.pedramrn.slick.parent.ui.BottomNavigationHandlerImpl;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.Navigator2;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;
import com.github.pedramrn.slick.parent.ui.custom.ImageViewLoader;
import com.github.pedramrn.slick.parent.ui.details.item.ItemComment;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCommentProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemHeader;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.MovieProvider;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.github.pedramrn.slick.parent.ui.list.ControllerList;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.slick.Presenter;
import com.github.slick.middleware.RequestStack;
import com.jakewharton.rxbinding2.support.design.widget.RxSnackbar;
import com.jakewharton.rxbinding2.view.RxView;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-28
 */

public class ControllerDetails extends ControllerElm<ViewStateDetails> implements ViewDetails, MovieProvider {

    private static final String TAG = ControllerDetails.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;

    @Inject
    Provider<PresenterDetails> provider;
    @Presenter
    PresenterDetails presenter;

    @Inject
    BottomNavigationHandlerImpl bottomNavigationHandler;

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
    private PublishSubject<String> onRetry = PublishSubject.create();
    private PublishSubject<Object> onErrorDismissed = PublishSubject.create();
    private FloatingActionButton fab;
    private Drawable drawableUnFav;
    private Drawable drawableFav;
    private Snackbar snackbar;

    private Snackbar.Callback callback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar transientBottomBar, int event) {
            onErrorDismissed.onNext(1);
            onRetry("ALL");
        }
    };
    private GroupAdapter adapterSimilar;
    private GroupAdapter adapterBackdrops;

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

    @Override
    protected void onAttach(@NonNull View view) {
        Navigator2.bind(this);
        RequestStack.getInstance().processLastRequest();
        //noinspection ConstantConditions
        snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_LONG)
                .setAction("Retry?", v -> snackbar.dismiss())
                .addCallback(callback)
                .setDuration(60_000)
        ;
    }

    @Override
    protected void onDetach(@NonNull View view) {
        Navigator2.unbindController();
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
        fab = binding.fab;
        if (drawableFav == null) {
            Resources resources = getResources();
            drawableUnFav = ResourcesCompat.getDrawable(resources, R.drawable.ic_unlike_black_24dp, null);
            drawableFav = ResourcesCompat.getDrawable(resources, R.drawable.ic_like_red_24dp, null);
        }

        final Context context = getApplicationContext();

        adapterMain = new GroupAdapter();
        adapterSimilar = new GroupAdapter();
        adapterBackdrops = new GroupAdapter();

        setOnItemClickListener(adapterMain);
        setOnItemClickListener(adapterSimilar);
        setOnItemClickListener(adapterBackdrops);

        updatingHeader = new UpdatingGroup();
        progressiveCast = new UpdatingGroup();
        progressiveSimilar = new UpdatingGroup();
        progressiveBackdrop = new UpdatingGroup();
        progressiveComments = new UpdatingGroup();

        adapterMain.setSpanCount(6);

        itemCardListSimilar = new ItemCardList(context, adapterSimilar, "SIMILAR");
        adapterSimilar.add(progressiveSimilar);

        Section sectionSimilar = new Section(new ItemCardHeader(100, "Similar"));
        sectionSimilar.add(itemCardListSimilar);

        headerCast = new ItemCardHeader(101, "Casts", "See All");
        headerCast.setOnClickListener(o -> {
            if (state.movieBasic() instanceof Movie && !((Movie) state.movieBasic()).casts().isEmpty()) {
                ArrayList<Cast> casts = (ArrayList<Cast>) ((Movie) state.movieBasic()).casts();
                ControllerList.start(getRouter(), state.movieBasic().title() + "'s Casts", new ArrayList<>(casts));
            }
        });
        Section sectionCasts = new Section(headerCast);
        sectionCasts.add(progressiveCast);

        headerComments = new ItemCardHeader(102, "Comments", "See All");
        headerComments.setOnClickListener(o -> {
            if (!state.comments().isEmpty() && !(state.comments().get(0) instanceof ItemCommentProgressive)) {
                ControllerList.start(ControllerDetails.this.getRouter(), "Comments for " + movie.title(), comments());
            }
        });
        Section sectionComments = new Section(headerComments);
        sectionComments.add(progressiveComments);

        itemBackdropList = new ItemListHorizontal(adapterBackdrops, "BACKDROPS");
        adapterBackdrops.add(progressiveBackdrop);

        Section sectionBackdrops = new Section(new ItemCardHeader(103, "Backdrops"));
        sectionBackdrops.add(itemBackdropList);

        sectionOverview = new Section(new ItemCardHeader(104, "Overview"));


        adapterMain.add(updatingHeader);
        adapterMain.add(sectionOverview);
        adapterMain.add(sectionCasts);
        adapterMain.add(sectionComments);
        adapterMain.add(sectionBackdrops);
        adapterMain.add(sectionSimilar);

        GridLayoutManager lm = new GridLayoutManager(context, adapterMain.getSpanCount(), LinearLayoutManager.VERTICAL, false);
        lm.setSpanSizeLookup(adapterMain.getSpanSizeLookup());
        binding.recyclerViewDetails.setLayoutManager(lm);
        binding.recyclerViewDetails.setAdapter(adapterMain);
        binding.recyclerViewDetails.getItemAnimator().setChangeDuration(0);
        binding.recyclerViewDetails.getItemAnimator().setMoveDuration(0);


        //        add(bottomNavigationHandler.handle((BottomBarHost) getParentController(), binding.recyclerViewDetails));

        presenter.updateStream().subscribe(this);

        /*fab.setOnLongClickListener(v -> {
            Crashlytics.getInstance().crash();
            return false;
        });*/

        return binding.getRoot();
    }

    @Override
    public void render(ViewStateDetails state) {
        this.state = state;
        long before = System.currentTimeMillis();

        movie = state.movieBasic();

        collapsingToolbar.setTitle(movie.title());
        imageViewHeader.loadBlur(movie.thumbnailBackdrop());

        updatingHeader.update(Collections.singletonList(new ItemHeader(movie, transitionName)));

        if (sectionOverview.getGroup(1) == null && movie.overview() != null) {
            sectionOverview.add(new ItemOverview(movie.overview()));
        }

        progressiveCast.update(state.casts());
        progressiveBackdrop.update(state.backdrops());
        progressiveSimilar.update(state.similar());

        List<Item> comments = Observable.fromIterable(state.comments())
                .take(2)
                .buffer(2)
                .blockingFirst(Collections.emptyList());
        //only show the first 2 items
        progressiveComments.update(comments);

        renderError(state.errorFavorite());

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
        super.onDestroyView(view);
        adapterBackdrops.setOnItemClickListener(null);
        adapterSimilar.setOnItemClickListener(null);
        adapterMain.setOnItemClickListener(null);
        itemCardListSimilar.onDestroyView();
        itemBackdropList.onDestroyView();
        headerCast.onDestroyView();
        headerComments.onDestroyView();

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
        headerCast = null;
        snackbar = null;

        adapterBackdrops = null;
        adapterSimilar = null;

        adapterMain.clear();
        adapterMain = null;

        fab.setOnClickListener(null);
        fab = null;

        super.onDestroyView(view);
    }

    @Override
    public MovieBasic get() {
        return movie;
    }

    @NonNull
    protected ArrayList<ItemViewListParcelable> comments() {
        // FIXME: 2017-11-13 store raw comments in view state with a command
        return (ArrayList<ItemViewListParcelable>) Observable.fromIterable(state.comments())
                .cast(ItemComment.class)
                .map(ItemComment::comment)
                .cast(ItemViewListParcelable.class)
                .buffer(state.comments().size())
                .onErrorReturnItem(Collections.emptyList())
                .blockingFirst(Collections.emptyList());
    }

    private void setOnItemClickListener(final GroupAdapter adapter) {
        adapter.setOnItemClickListener((item, view) -> ((OnItemAction) item)
                .action(ControllerDetails.this, null, adapter.getAdapterPosition(item)));
    }

    @Override
    public Observable<Boolean> commandFavorite() {
        return RxView.clicks(fab).throttleLast(1, TimeUnit.SECONDS)
                .map(o -> drawableUnFav.equals(fab.getDrawable()))
                ;
    }

    @Override
    public Observable<Object> onRetryComments() {
        return onRetry.filter("COMMENTS_ERROR"::equals).cast(Object.class);
    }

    @Override
    public Observable<Object> onRetryAll() {
        return onRetry.cast(Object.class);
    }

    @Override
    public Observable<Object> errorDismissed() {
        return RxSnackbar.dismisses(snackbar).cast(Object.class);
    }

    @Override
    public void notFavorite() {
        fab.setImageDrawable(drawableUnFav);
    }

    @Override
    public void favorite() {
        fab.setImageDrawable(drawableFav);
    }

    @Override
    public void onRetry(String tag) {
        onRetry.onNext(tag);
    }

    @Override
    public void error(short code) {
        snackbar.setText(ErrorHandler.message(getApplicationContext(), code)).show();
    }
}
