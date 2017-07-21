package com.github.pedramrn.slick.parent.ui.home;

import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAnticipated;
import com.github.pedramrn.slick.parent.domain.router.RouterAnticipatedImpl;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.home.item.ItemView;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterMovieDetailsVideoImpl;
import com.github.pedramrn.slick.parent.ui.home.router.RouterPopularImpl;
import com.github.pedramrn.slick.parent.ui.home.router.RouterTrendingImpl;
import com.github.pedramrn.slick.parent.ui.home.router.RouterUpcomingImpl;
import com.github.pedramrn.slick.parent.ui.home.state.PartialViewStateHome;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.util.IdBank;
import com.github.pedramrn.slick.parent.util.ScanList;
import com.github.slick.SlickPresenter;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class PresenterHome extends SlickPresenter<ViewHome> implements Observer<ViewStateHome> {

    private final RouterMovieDetails router;
    private final RouterAnticipated routerAnticipated;
    private final RouterTrendingImpl routerTrending;
    private final RouterUpcomingImpl routerUpcoming;
    private final MapperMovieDomainMovie mapper;
    private final RouterPopularImpl routerPopular;
    private final MapperMovieSmallDomainMovieSmall mapperMovieSmall;
    private final Scheduler io;
    private final Scheduler main;
    private BehaviorSubject<ViewStateHome> state = BehaviorSubject.create();
    private BehaviorSubject<Integer> triggerSubjectTrending = BehaviorSubject.create();
    private BehaviorSubject<Integer> triggerSubjectPopular = BehaviorSubject.create();
    private Observable<ViewStateHome> home;
    private Disposable disposable;
    private String TRENDING = "TRENDING";
    private String POPULAR = "POPULAR";
    private String BANNER = "BANNER";


    @Inject
    public PresenterHome(RouterMovieDetailsVideoImpl routerMovieDetailsVideo,
                         RouterAnticipatedImpl routerAnticipated,
                         RouterTrendingImpl routerTrending,
                         RouterPopularImpl routerPopular,
                         RouterUpcomingImpl routerUpcoming,
                         MapperMovieDomainMovie mapper,
                         MapperMovieSmallDomainMovieSmall mapperMovieSmall,
                         @Named("io") Scheduler io,
                         @Named("main") Scheduler main) {
        this.router = routerMovieDetailsVideo;
        this.routerAnticipated = routerAnticipated;
        this.routerTrending = routerTrending;
        this.routerUpcoming = routerUpcoming;
        this.mapper = mapper;
        this.routerPopular = routerPopular;
        this.mapperMovieSmall = mapperMovieSmall;
        this.io = io;
        this.main = main;
    }

    public Observable<ViewStateHome> updateStream(int pageSize) {
        if (home == null) start(triggerSubjectTrending.startWith(1), triggerSubjectPopular.startWith(1), pageSize);
        return state;
    }

    public void start(Observable<Integer> triggerTrending,
                      Observable<Integer> triggerPopular,
                      final int pageSize) {
        IdBank.reset(BANNER);
        IdBank.reset(TRENDING);
        IdBank.reset(POPULAR);

        Observable<PartialViewState<ViewStateHome>> upcoming = routerUpcoming.upcoming(1)
                .concatMap(new Function<PagedDomain<MovieSmallDomain>, ObservableSource<MovieSmallDomain>>() {
                    @Override
                    public ObservableSource<MovieSmallDomain> apply(@NonNull PagedDomain<MovieSmallDomain> pagedDomain) throws Exception {
                        return Observable.fromIterable(pagedDomain.data());
                    }
                })
                .map(mapperMovieSmall)
                .map(new MapProgressive())
                .cast(MovieSmall.class)
                .map(new Function<MovieSmall, Item>() {
                    @Override
                    public Item apply(@NonNull final MovieSmall movieSmall) throws Exception {
                        return movieSmall.render(BANNER);
                    }
                })
                .buffer(20)
                .compose(new ScanList<Item>())
                .map(new Function<List<Item>, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull List<Item> items) throws Exception {
                        return new PartialViewStateHome.Upcoming(items);
                    }
                })
                .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull Throwable throwable) throws Exception {
                        return new PartialViewStateHome.UpcomingError(throwable);
                    }
                })
                .startWith(new PartialViewStateHome.ProgressiveBannerImpl(2, BANNER))
                .subscribeOn(io);

        // TODO: 2017-07-08 extract a transformer
        // FIXME: 2017-07-08 trending items order changes frequently and we end up with next page and duplicate items, which causes wrong list animations on every update
        Observable<PartialViewState<ViewStateHome>> trending = triggerTrending
                .concatMap(new Function<Integer, ObservableSource<MovieDomain>>() {
                    @Override
                    public ObservableSource<MovieDomain> apply(@NonNull Integer page) throws Exception {
                        return routerTrending.trending(page, pageSize).subscribeOn(io);
                    }
                })
                .map(mapper)
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(new Function<ItemView, Item>() {
                    @Override
                    public Item apply(@NonNull ItemView itemCard) throws Exception {
                        return itemCard.render(TRENDING);
                    }
                })
                .buffer(pageSize)
                .compose(new ScanList<Item>())
                .map(new Function<List<Item>, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull List<Item> movies) throws Exception {
                        return new PartialViewStateHome.Trending(movies, false);
                    }
                })
                .startWith(new PartialViewStateHome.CardProgressiveTrending(pageSize, TRENDING))
                .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull Throwable throwable) throws Exception {
                        return new PartialViewStateHome.Error(throwable);
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(io);

        // TODO: 2017-07-08 extract a transformer
        Observable<PartialViewState<ViewStateHome>> trendingProgressiveLoading = triggerTrending
                .skip(1)
                .map(new Function<Integer, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull Integer integer) throws Exception {
                        return new PartialViewStateHome.CardProgressiveTrending(pageSize, TRENDING);
                    }
                });

        Observable<PartialViewState<ViewStateHome>> popular = triggerPopular
                .concatMap(new Function<Integer, ObservableSource<MovieDomain>>() {
                    @Override
                    public ObservableSource<MovieDomain> apply(@NonNull Integer page) throws Exception {
                        return routerPopular.popular(page, pageSize).subscribeOn(io);
                    }
                })
                .map(mapper)
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(new Function<ItemView, Item>() {
                    @Override
                    public Item apply(@NonNull ItemView itemCard) throws Exception {
                        return itemCard.render(POPULAR);
                    }
                })
                .buffer(pageSize)
                .compose(new ScanList<Item>())
                .map(new Function<List<Item>, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull List<Item> movies) throws Exception {
                        return new PartialViewStateHome.Popular(movies, false);
                    }
                })
                .startWith(new PartialViewStateHome.CardProgressivePopular(pageSize, POPULAR))
                .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull Throwable throwable) throws Exception {
                        return new PartialViewStateHome.Error(throwable);
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(io);

        Observable<PartialViewState<ViewStateHome>> popularProgressiveLoading = triggerPopular
                .skip(1)
                .map(new Function<Integer, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull Integer integer) throws Exception {
                        return new PartialViewStateHome.CardProgressivePopular(pageSize, POPULAR);
                    }
                });

        /*Observable<PartialViewState<ViewStateHome>> click = command.map(new Function<Object, PartialViewState<ViewStateHome>>() {
            @Override
            public PartialViewState<ViewStateHome> apply(@NonNull Object o) throws Exception {
                return new PartialViewState<ViewStateHome>() {
                    @Override
                    public ViewStateHome reduce(ViewStateHome viewStateHome) {
                        return viewStateHome;
                    }
                };
            }
        });*/

        List<Observable<PartialViewState<ViewStateHome>>> list = new ArrayList<>(5);
        list.add(upcoming);
        list.add(trending);
        list.add(popular);
        list.add(trendingProgressiveLoading);
        list.add(popularProgressiveLoading);
        // list.add(click);

        ViewStateHome initialState = ViewStateHome.builder()
                .upcoming(Collections.<Item>emptyList())
                .popular(Collections.<Item>emptyList())
                .trending(Collections.<Item>emptyList())
                .anticipated(Collections.<Item>emptyList())
                .loadingTrending(true)
                .loadingPopular(true)
                .itemLoadingCountPopular(0)
                .itemLoadingCountTrending(0)
                .pagePopular(0)
                .pageTrending(0)
                .build();

        home = Observable.merge(list)
                .observeOn(main)
                .scan(initialState, new BiFunction<ViewStateHome, PartialViewState<ViewStateHome>, ViewStateHome>() {
                    @Override
                    public ViewStateHome apply(@NonNull ViewStateHome viewStateHome, @NonNull PartialViewState<ViewStateHome> vp) throws Exception {
                        Log.e(TAG, Thread.currentThread().getName());
                        return vp.reduce(viewStateHome);
                    }
                });

        home.subscribe(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
        dispose(disposable);
        this.disposable = d;
    }

    @Override
    public void onNext(ViewStateHome viewStateHome) {
        state.onNext(viewStateHome);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.wtf(TAG, "onCompleteGlide: Called O_O");
    }

    private static final String TAG = PresenterHome.class.getSimpleName();

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose(disposable);
        IdBank.dispose(TRENDING);
        IdBank.dispose(POPULAR);
    }

    private void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public Observer<Integer> onLoadMoreObserverPoplar() {
        return triggerSubjectPopular;
    }

    public Observer<Integer> onLoadMoreObserverTrending() {
        return triggerSubjectTrending;
    }
}
