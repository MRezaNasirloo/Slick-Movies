package com.github.pedramrn.slick.parent.ui.home;

import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.VideoDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAnticipated;
import com.github.pedramrn.slick.parent.domain.router.RouterAnticipatedImpl;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterPopularImpl;
import com.github.pedramrn.slick.parent.domain.router.RouterTrendingImpl;
import com.github.pedramrn.slick.parent.ui.details.mapper.MovieDomainMovieMapper;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.github.pedramrn.slick.parent.ui.home.router.RouterMovieDetailsVideoImpl;
import com.github.pedramrn.slick.parent.util.ScanList;
import com.github.pedramrn.slick.parent.util.ScanToList;
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
    private final MovieDomainMovieMapper mapper;
    private final RouterPopularImpl routerPopular;
    private final Scheduler io;
    private final Scheduler main;
    private BehaviorSubject<ViewStateHome> state = BehaviorSubject.create();
    private BehaviorSubject<Integer> triggerSubjectTrending = BehaviorSubject.create();
    private BehaviorSubject<Integer> triggerSubjectPopular = BehaviorSubject.create();
    private Observable<ViewStateHome> home;
    private Disposable disposable;
    private String TRENDING = "TRENDING";
    private String POPULAR = "POPULAR";


    @Inject
    public PresenterHome(RouterMovieDetailsVideoImpl rmd,
                         RouterAnticipatedImpl ra,
                         RouterTrendingImpl rt,
                         RouterPopularImpl rp,
                         MovieDomainMovieMapper mapper,
                         @Named("io") Scheduler io,
                         @Named("main") Scheduler main) {
        this.router = rmd;
        this.routerAnticipated = ra;
        this.routerTrending = rt;
        this.mapper = mapper;
        this.routerPopular = rp;
        this.io = io;
        this.main = main;
    }

    public Observable<ViewStateHome> updateStream(int pageSize, Observable<Object> clickListener) {
        if (home == null) start(triggerSubjectTrending.startWith(1), triggerSubjectPopular.startWith(1), clickListener, pageSize);
        return state;
    }

    public void start(Observable<Integer> triggerTrending, Observable<Integer> triggerPopular, Observable<Object> clickListener, final int pageSize) {
        IdBank.reset(TRENDING);
        IdBank.reset(POPULAR);

        Observable<ViewStateHomePartial> videos = routerAnticipated.anticipated3()
                .map(new Function<VideoDomain, ItemVideo>() {
                    @Override
                    public ItemVideo apply(@NonNull VideoDomain vd) throws Exception {
                        return Video.create(vd.tmdb(), vd.type(), vd.key(), vd.name());
                    }
                })
                .map(new Function<ItemVideo, Item>() {
                    @Override
                    public Item apply(@NonNull ItemVideo itemVideo) throws Exception {
                        return itemVideo.render(-1);
                    }
                })
                .compose(new ScanToList<Item>())
                .map(new Function<List<Item>, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull List<Item> itemHomes) throws Exception {
                        return new ViewStateHomePartial.VideosImpl(itemHomes);
                    }
                })
                .startWith(new ViewStateHomePartial.ProgressiveVideosImpl())
                .onErrorReturn(new Function<Throwable, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull Throwable throwable) throws Exception {
                        return new ViewStateHomePartial.VideosErrorImpl(throwable);
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(io);

        // TODO: 2017-07-08 extract a transformer
        // FIXME: 2017-07-08 trending items order changes frequently and we end up with next page and duplicate items, which causes wrong list animations on every update
        Observable<ViewStateHomePartial> trending = triggerTrending
                .concatMap(new Function<Integer, ObservableSource<MovieDomain>>() {
                    @Override
                    public ObservableSource<MovieDomain> apply(@NonNull Integer page) throws Exception {
                        return routerTrending.trending(page, pageSize).subscribeOn(io);
                    }
                })
                .map(mapper)
                .map(new MapProgressive())
                .cast(ItemCard.class)
                .map(new Function<ItemCard, Item>() {
                    @Override
                    public Item apply(@NonNull ItemCard itemCard) throws Exception {
                        return itemCard.render(TRENDING);
                    }
                })
                .buffer(pageSize)
                .compose(new ScanList<Item>())
                .map(new Function<List<Item>, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull List<Item> movies) throws Exception {
                        return new ViewStateHomePartial.Trending(movies, false);
                    }
                })
                .startWith(new ViewStateHomePartial.CardProgressiveTrending(pageSize, TRENDING))
                .onErrorReturn(new Function<Throwable, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull Throwable throwable) throws Exception {
                        return new ViewStateHomePartial.Error(throwable);
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(io);

        // TODO: 2017-07-08 extract a transformer
        Observable<ViewStateHomePartial> trendingProgressiveLoading = triggerTrending
                .skip(1)
                .map(new Function<Integer, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull Integer integer) throws Exception {
                        return new ViewStateHomePartial.CardProgressiveTrending(pageSize, TRENDING);
                    }
                });

        Observable<ViewStateHomePartial> popular = triggerPopular
                .concatMap(new Function<Integer, ObservableSource<MovieDomain>>() {
                    @Override
                    public ObservableSource<MovieDomain> apply(@NonNull Integer page) throws Exception {
                        return routerPopular.popular(page, pageSize).subscribeOn(io);
                    }
                })
                .map(mapper)
                .map(new MapProgressive())
                .cast(ItemCard.class)
                .map(new Function<ItemCard, Item>() {
                    @Override
                    public Item apply(@NonNull ItemCard itemCard) throws Exception {
                        return itemCard.render(POPULAR);
                    }
                })
                .buffer(pageSize)
                .compose(new ScanList<Item>())
                .map(new Function<List<Item>, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull List<Item> movies) throws Exception {
                        return new ViewStateHomePartial.Popular(movies, false);
                    }
                })
                .startWith(new ViewStateHomePartial.CardProgressivePopular(pageSize, POPULAR))
                .onErrorReturn(new Function<Throwable, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull Throwable throwable) throws Exception {
                        return new ViewStateHomePartial.Error(throwable);
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(io);

        Observable<ViewStateHomePartial> popularProgressiveLoading = triggerPopular
                .skip(1)
                .map(new Function<Integer, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull Integer integer) throws Exception {
                        return new ViewStateHomePartial.CardProgressivePopular(pageSize, POPULAR);
                    }
                });

        Observable<ViewStateHomePartial> click = clickListener.map(new Function<Object, ViewStateHomePartial>() {
            @Override
            public ViewStateHomePartial apply(@NonNull Object o) throws Exception {
                return new ViewStateHomePartial() {
                    @Override
                    public ViewStateHome reduce(ViewStateHome viewStateHome) {
                        return viewStateHome;
                    }
                };
            }
        });

        List<Observable<ViewStateHomePartial>> list = new ArrayList<>(5);
        list.add(trending);
        list.add(popular);
        list.add(trendingProgressiveLoading);
        list.add(popularProgressiveLoading);
        list.add(videos);
        list.add(click);

        ViewStateHome initialState = ViewStateHome.builder()
                .anticipated(Collections.<Item>emptyList())
                .popular(Collections.<Item>emptyList())
                .trending(Collections.<Item>emptyList())
                .loadingTrending(true)
                .loadingPopular(true)
                .itemLoadingCountPopular(0)
                .itemLoadingCountTrending(0)
                .pagePopular(0)
                .pageTrending(0)
                .build();

        home = Observable.merge(list)
                .observeOn(main)
                .scan(initialState, new BiFunction<ViewStateHome, ViewStateHomePartial, ViewStateHome>() {
                    @Override
                    public ViewStateHome apply(@NonNull ViewStateHome viewStateHome, @NonNull ViewStateHomePartial vp) throws Exception {
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
