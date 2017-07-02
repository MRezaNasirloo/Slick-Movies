package com.github.pedramrn.slick.parent.ui.home;

import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.VideoDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAnticipated;
import com.github.pedramrn.slick.parent.domain.router.RouterAnticipatedImpl;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterPopularImpl;
import com.github.pedramrn.slick.parent.domain.router.RouterTrendingImpl;
import com.github.pedramrn.slick.parent.ui.details.mapper.MovieDomainMovieMapper;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.github.pedramrn.slick.parent.ui.home.router.RouterMovieDetailsVideoImpl;
import com.github.pedramrn.slick.parent.util.ListToObserable;
import com.github.pedramrn.slick.parent.util.Scan;
import com.github.slick.SlickPresenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
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
    private Observable<ViewStateHome> home;
    private Disposable disposable;


    @Inject
    public PresenterHome(RouterMovieDetailsVideoImpl rmd, RouterAnticipatedImpl ra, RouterTrendingImpl rt, MovieDomainMovieMapper mapper,
                         RouterPopularImpl rp, @Named("io") Scheduler io, @Named("main") Scheduler main) {
        this.router = rmd;
        this.routerAnticipated = ra;
        this.routerTrending = rt;
        this.mapper = mapper;
        this.routerPopular = rp;
        this.io = io;
        this.main = main;
    }


    public Observable<ViewStateHome> updateStream() {
        if (home == null) start();
        return state;
    }

    public void start() {
        Observable<ViewStateHomePartial> videos =
                routerAnticipated.anticipated3().map(new Function<VideoDomain, ItemVideo>() {
                    @Override
                    public ItemVideo apply(@NonNull VideoDomain vd) throws Exception {
                        return Video.create(vd.tmdb(), vd.type(), vd.key(), vd.name());
                    }
                }).compose(new Scan<ItemVideo>())
                        .map(new Function<List<ItemVideo>, ViewStateHomePartial>() {
                            @Override
                            public ViewStateHomePartial apply(@NonNull List<ItemVideo> itemHomes) throws Exception {
                                return new ViewStateHomePartial.VideosImpl(itemHomes);
                            }
                        })
                        .startWith(new ViewStateHomePartial.ProgressiveVideosImpl())
                        .onErrorReturn(new Function<Throwable, ViewStateHomePartial>() {
                            @Override
                            public ViewStateHomePartial apply(@NonNull Throwable throwable) throws Exception {
                                return new ViewStateHomePartial.VideosErrorImpl(throwable);
                            }
                        }).subscribeOn(io);

        Observable<ViewStateHomePartial> trending = routerTrending.trending(1, 20)
                .map(mapper)
                .map(new MapProgressive(3))// TODO: 2017-07-02 this 3 numbers should be related to the size of screen width
                .cast(Movie.class)
                .buffer(3)
                .flatMap(new ListToObserable<Movie>())
                .compose(new Scan<ItemCard>())
                .map(new Function<List<ItemCard>, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull List<ItemCard> movies) throws Exception {
                        return new ViewStateHomePartial.Trending(movies);
                    }
                })
                .startWith(new ViewStateHomePartial.CardProgressiveTrending())
                .onErrorReturn(new Function<Throwable, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull Throwable throwable) throws Exception {
                        return new ViewStateHomePartial.Error(throwable);
                    }
                })
                .subscribeOn(io);

        Observable<ViewStateHomePartial> popular = routerPopular.popular(1, 20)
                .map(mapper)
                .map(new MapProgressive(3))// TODO: 2017-07-02 this 3 numbers should be related to the size of screen width
                .cast(Movie.class)
                .buffer(3)
                .flatMap(new ListToObserable<Movie>())
                .compose(new Scan<ItemCard>())
                .map(new Function<List<ItemCard>, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull List<ItemCard> movies) throws Exception {
                        return new ViewStateHomePartial.Popular(movies);
                    }
                })
                .startWith(new ViewStateHomePartial.CardProgressivePopular())
                .onErrorReturn(new Function<Throwable, ViewStateHomePartial>() {
                    @Override
                    public ViewStateHomePartial apply(@NonNull Throwable throwable) throws Exception {
                        return new ViewStateHomePartial.Error(throwable);
                    }
                })
                .subscribeOn(io);

        home = Observable.merge(trending, videos, popular)
                .observeOn(main)
                .scan(ViewStateHome.builder().build(), new BiFunction<ViewStateHome, ViewStateHomePartial, ViewStateHome>() {
                    @Override
                    public ViewStateHome apply(@NonNull ViewStateHome viewStateHome, @NonNull ViewStateHomePartial vp) throws Exception {
                        return vp.reduce(viewStateHome);
                    }
                });
        home.subscribe(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
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
        Log.wtf(TAG, "onComplete: Called O_O");
    }

    private static final String TAG = PresenterHome.class.getSimpleName();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
