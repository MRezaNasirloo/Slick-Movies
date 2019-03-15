package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerDetailsBinding;
import com.github.pedramrn.slick.parent.ui.BottomNavigationHandlerImpl;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.Navigator2;
import com.github.pedramrn.slick.parent.ui.Screen;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;
import com.github.pedramrn.slick.parent.ui.custom.ImageViewLoader;
import com.github.pedramrn.slick.parent.ui.details.favorite.FloatingFavorite;
import com.github.pedramrn.slick.parent.ui.details.favorite.PresenterFloatingFavorite_Slick;
import com.github.pedramrn.slick.parent.ui.details.item.ExpandableReleaseDate;
import com.github.pedramrn.slick.parent.ui.details.item.ItemComment;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCommentProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemHeader;
import com.github.pedramrn.slick.parent.ui.details.item.ItemListHorizontal;
import com.github.pedramrn.slick.parent.ui.details.item.ItemOverview;
import com.github.pedramrn.slick.parent.ui.details.item.ItemSpace;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.FragmentBase;
import com.github.pedramrn.slick.parent.ui.home.MovieProvider;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardHeader;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardList;
import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.github.pedramrn.slick.parent.ui.list.ControllerList;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.videos.PresenterVideos;
import com.github.pedramrn.slick.parent.ui.videos.PresenterVideos_Slick;
import com.github.pedramrn.slick.parent.ui.videos.ViewVideos;
import com.jakewharton.rxbinding2.support.design.widget.RxSnackbar;
import com.mrezanasirloo.slick.Presenter;
import com.orhanobut.logger.Logger;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.Section;
import com.xwray.groupie.UpdatingGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-04-28
 */

public class ControllerDetails extends FragmentBase implements ViewDetails, ViewVideos, MovieProvider, Screen {

    private static final String TAG = ControllerDetails.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;

    @Inject
    Provider<PresenterDetails> provider;
    @Presenter
    PresenterDetails presenter;

    @Inject
    public Provider<PresenterVideos> providerVideo;
    @Presenter
    public PresenterVideos presenterVideo;

    @Inject
    BottomNavigationHandlerImpl bottomNavigationHandler;

    private MovieBasic movie;
    private String transitionName;

    private Section progressiveComments;
    private Section progressiveCast;
    private Section updatingHeader;
    private UpdatingGroup releaseDates;
    private ExpandableGroup expandableReleaseDate = new ExpandableGroup(new ExpandableReleaseDate());

    //    private ItemListHorizontal itemHeader;
    private ItemListHorizontal itemBackdropList;
    private ItemListHorizontal itemVideos;
    private ItemCardList itemCardListSimilar;

    private Section sectionOverview;

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageViewLoader imageViewHeader;

    private ViewStateDetails state;
    private Snackbar snackbar;
    private FloatingFavorite fab;
    private GroupAdapter adapterMain;
    private ItemCardHeader headerCast;
    private ItemCardHeader headerComments;
    private final PublishSubject<Object> onRetry = PublishSubject.create();
    private final PublishSubject<Object> onErrorDismissed = PublishSubject.create();
    private final PublishSubject<MovieBasic> moviePublish = PublishSubject.create();

    private Snackbar.Callback callback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar transientBottomBar, int event) {
            onErrorDismissed.onNext(1);
            if (DISMISS_EVENT_TIMEOUT == event || DISMISS_EVENT_ACTION == event) onRetry("ALL");
        }
    };
    private GroupAdapter adapterSimilar;
    private GroupAdapter adapterBackdrops;
    private GroupAdapter adapterVideos;

    public static Screen newInstance(@NonNull MovieBasic movie, @Nullable String transitionName) {
        Bundle bundle = new BundleBuilder(new Bundle())
                .putParcelable("ITEM", movie)
                .putString("IMDB_ID", movie.imdbId())
                .putString("TRANSITION_NAME", transitionName)
                .build();
        ControllerDetails fragment = new ControllerDetails();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Screen newInstance(@NonNull String imdbId, @Nullable String transitionName) {
        Bundle bundle = new BundleBuilder(new Bundle())
                .putParcelable("ITEM", null)
                .putString("IMDB_ID", imdbId)
                .putString("TRANSITION_NAME", transitionName)
                .build();
        ControllerDetails fragment = new ControllerDetails();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        transitionName = arguments.getString("TRANSITION_NAME");
        String imdbId = arguments.getString("IMDB_ID");
        movie = arguments.getParcelable("ITEM");
        if (movie == null) { // // FIXME: 2018-04-09 this logic should be in the presenter
            movie = MovieSmall.builder()
                    .imdbId(imdbId)
                    .id(-1)
                    .uniqueId(-1)
                    .build();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("LOG_IT_onAttach() called");
        //noinspection ConstantConditions
        snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_LONG)
                .setAction("Retry?", v -> snackbar.dismiss())
                .addCallback(callback)
                .setDuration(60_000)
        ;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        System.out.println("LOG_IT_onCreateView() called");
        Navigator2.bind(this);
        App.componentMain().inject(this);
        PresenterDetails_Slick.bind(this);
        PresenterVideos_Slick.bind(this);
        ControllerDetailsBinding binding = ControllerDetailsBinding.inflate(inflater, container, false);
        if (getActivity() != null) {
            ((ToolbarHost) getActivity()).setToolbar(binding.toolbar).setupButton(binding.toolbar, true);
        }

        collapsingToolbar = binding.collapsingToolbar;
        imageViewHeader = binding.imageViewHeader;
        fab = binding.floatingFab;
        fab.onBind(getInstanceId());

        final Context context = getContext().getApplicationContext();

        adapterMain = new GroupAdapter();
        adapterVideos = new GroupAdapter();
        adapterSimilar = new GroupAdapter();
        adapterBackdrops = new GroupAdapter();

        setOnItemClickListener(adapterMain);
        setOnItemClickListener(adapterVideos);
        setOnItemClickListener(adapterSimilar);
        setOnItemClickListener(adapterBackdrops);

        releaseDates = new UpdatingGroup();
        updatingHeader = new Section();
        progressiveCast = new Section();
        progressiveComments = new Section();

        expandableReleaseDate.add(releaseDates);

        adapterMain.setSpanCount(6);

        // releaseDates.setHeader(new ItemCardHeader(90, "Release Dates"));

        itemCardListSimilar = new ItemCardList(context, adapterSimilar, "SIMILAR");

        Section sectionSimilar = new Section(new ItemCardHeader(100, "Similar"));
        sectionSimilar.add(itemCardListSimilar);

        headerCast = new ItemCardHeader(101, "Casts", "See All");
        headerCast.setOnClickListener(o -> {
            if (state.movieBasic() instanceof Movie && !((Movie) state.movieBasic()).casts().isEmpty()) {
                ArrayList<Cast> casts = (ArrayList<Cast>) ((Movie) state.movieBasic()).casts();
                navigateTo(ControllerList.newInstance(state.movieBasic().title() + "'s Casts", new ArrayList<>(casts)));
            }
        });
        progressiveCast.setHeader(headerCast);

        headerComments = new ItemCardHeader(102, "Comments", "See All");
        headerComments.setOnClickListener(o -> {
            if (!state.comments().isEmpty() && !(state.comments().get(0) instanceof ItemCommentProgressive)) {
                navigateTo(ControllerList.newInstance("Comments for " + movie.title(), comments()));
            }
        });
        Section sectionComments = new Section(headerComments);
        sectionComments.add(progressiveComments);

        itemBackdropList = new ItemListHorizontal(adapterBackdrops, "BACKDROPS");

        Section sectionBackdrops = new Section(new ItemCardHeader(103, "Backdrops"));
        sectionBackdrops.add(itemBackdropList);

        itemVideos = new ItemListHorizontal(adapterVideos, "VIDEOS");

        Section sectionVideos = new Section(new ItemCardHeader(104, "Videos"));
        sectionVideos.add(itemVideos);

        sectionOverview = new Section(new ItemCardHeader(105, "Overview"));

        adapterMain.add(updatingHeader);
        adapterMain.add(sectionOverview);
        adapterMain.add(progressiveCast);
        adapterMain.add(sectionVideos);
        adapterMain.add(sectionComments);
        adapterMain.add(sectionBackdrops);
        adapterMain.add(expandableReleaseDate);
        adapterMain.add(sectionSimilar);
        adapterMain.add(new ItemSpace());

        GridLayoutManager lm = new GridLayoutManager(context, adapterMain.getSpanCount(), LinearLayoutManager.VERTICAL, false);
        lm.setSpanSizeLookup(adapterMain.getSpanSizeLookup());
        binding.recyclerViewDetails.setLayoutManager(lm);
        binding.recyclerViewDetails.setAdapter(adapterMain);
        binding.recyclerViewDetails.getItemAnimator().setChangeDuration(0);
        binding.recyclerViewDetails.getItemAnimator().setMoveDuration(0);


        //        add(bottomNavigationHandler.handle((BottomBarHost) getParentController(), binding.recyclerViewDetails));

        /*fab.setOnLongClickListener(v -> {
            Crashlytics.getInstance().crash();
            return false;
        });*/

        updatingHeader.update(Collections.singletonList(new ItemHeader(movie, transitionName)));

        // Since updating the header while we are in a shared transition cases the animation to stop we have to wait until
        // the animation is stopped, weirdly shared animation callback didn't work all the time.
        add(moviePublish.distinct()
                    .delay(duration + 150, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                    .subscribe(movieBasic -> {
                        if (updatingHeader != null) {
                            updatingHeader.update(
                                    Collections.singletonList(new ItemHeader(movieBasic, transitionName)));
                        }
                    }));

        return binding.getRoot();
    }

    @Override
    public void render(ViewStateDetails state) {
        this.state = state;
        // long before = System.currentTimeMillis();

        movie = state.movieBasic();
        moviePublish.onNext(movie);

        collapsingToolbar.setTitle(movie.title());
        imageViewHeader.loadBlur(movie.thumbnailBackdrop());

        /*if (animationEnded) {
            updatingHeader.update(Collections.singletonList(new ItemHeader(this, movieObservable, transitionName)));
            Logger.d("Updated Header!");
        }*/

        if (movie.overview() != null) {
            sectionOverview.update(Collections.singletonList(new ItemOverview(movie.overview())));
        }

        progressiveCast.update(state.casts());
        adapterBackdrops.update(state.backdrops());
        adapterSimilar.update(state.similar());

        List<Item> comments = Observable.fromIterable(state.comments())
                .take(2)
                .buffer(2)
                .blockingFirst(Collections.emptyList());
        //only show the first 2 items
        progressiveComments.update(comments);

        renderError(state.errorFavorite());

        if (movie.id() != -1) fab.setMovie(movie);

        releaseDates.update(state.releaseDates());

        // long delay = System.currentTimeMillis() - before;
        // int sizeCast = state.casts().size();
        // int sizeBackdrops = state.backdrops().size();
        // int sizeSimilar = state.similar().size();

        // Log.e(TAG, String.format(Locale.ENGLISH, "casts: %d, backdrops: %d, similar: %d", sizeCast, sizeBackdrops,
        // sizeSimilar));
        // Log.e(TAG, String.format(Locale.ENGLISH, "Updating the list cost you %sms", delay));
    }

    @Override
    public MovieBasic getMovie() {
        return movie;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (itemBackdropList != null) {
            itemVideos.onSaveViewState(outState);
            itemBackdropList.onSaveViewState(outState);
            itemCardListSimilar.onSaveViewState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null || itemBackdropList == null) return;
        itemVideos.onRestoreViewState(savedInstanceState);
        itemBackdropList.onRestoreViewState(savedInstanceState);
        itemCardListSimilar.onRestoreViewState(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        System.out.println("LOG_IT_ControllerDetails.onDestroyView");
        super.onDestroyView();
        adapterBackdrops.setOnItemClickListener(null);
        adapterSimilar.setOnItemClickListener(null);
        adapterVideos.setOnItemClickListener(null);
        adapterMain.setOnItemClickListener(null);
        itemCardListSimilar.onDestroyView();
        itemBackdropList.onDestroyView();
        headerComments.onDestroyView();
        itemVideos.onDestroyView();
        headerCast.onDestroyView();
        fab.onDestroy();

        updatingHeader = null;
        progressiveCast = null;
        progressiveComments = null;

        itemCardListSimilar = null;
        itemBackdropList = null;
        sectionOverview = null;
        itemVideos = null;

        collapsingToolbar = null;
        imageViewHeader = null;

        headerComments = null;
        headerCast = null;
        snackbar = null;

        adapterBackdrops = null;
        adapterSimilar = null;
        adapterVideos = null;

        adapterMain.clear();
        adapterMain = null;

        fab = null;
        System.out.println("LOG_IT_isRemoving()= [" + isRemoving() + "]");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        System.out.println("LOG_IT_ControllerDetails.onDestroy");
        PresenterFloatingFavorite_Slick.onDestroy(getInstanceId(), getActivity());
        super.onDestroy();
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
                .action(ControllerDetails.this, this, null, adapter.getAdapterPosition(item), view));
    }

    @Override
    public Observable<Object> onRetryComments() {
        Logger.i("onRetryComments called");
        return onRetry.filter("COMMENTS_ERROR"::equals).cast(Object.class);
    }

    @NonNull
    @Override
    public Observable<Object> onRetryAll() {
        Logger.i("onRetryAll called");
        return onRetry.cast(Object.class).throttleLast(200, TimeUnit.MILLISECONDS);
    }

    @NonNull
    @Override
    public Observable<Object> errorDismissed() {
        return RxSnackbar.dismisses(snackbar).cast(Object.class);
    }

    @NonNull
    @Override
    public String viewType() {
        return PresenterVideos.VIDEO_IN_DETAILS_PAGE;
    }

    @NonNull
    @Override
    public Observable<MovieBasic> movieObservable() {
        return moviePublish.distinct();
    }

    @Override
    public void update(@NonNull List<Item> videos) {
        Iterator<Item> iterator = videos.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item instanceof ItemHeader) {
                iterator.remove();
            }
        }
        adapterVideos.update(videos);
    }

    @Override
    public void error(short code) {
        snackbar.setText(ErrorHandler.message(getContext().getApplicationContext(), code)).show();
    }

    @NonNull
    @Override
    public Observable<Object> onRetry() {
        return onRetry;
    }

    @Override
    public void onRetry(String tag) {
        Logger.i("retry called: %s", tag);
        onRetry.onNext(tag);
    }
}
