package com.github.pedramrn.slick.parent.ui.home;

import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.VideoDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAnticipated;
import com.github.pedramrn.slick.parent.domain.router.RouterAnticipatedImpl;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.github.pedramrn.slick.parent.ui.home.router.RouterMovieDetailsVideoImpl;
import com.github.pedramrn.slick.parent.util.Scan;
import com.github.slick.SlickPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class PresenterHome extends SlickPresenter<ViewHome> implements Observer<ViewStateHome> {

    private final RouterMovieDetails router;
    private final RouterAnticipated routerAnticipated;
    private final Scheduler io;
    private final Scheduler main;
    private BehaviorSubject<ViewStateHome> state = BehaviorSubject.create();
    private Observable<ViewStateHome> home;
    private Disposable disposable;


    @Inject
    public PresenterHome(RouterMovieDetailsVideoImpl rmd, RouterAnticipatedImpl ra, @Named("io") Scheduler io, @Named("main") Scheduler main) {
        this.router = rmd;
        this.routerAnticipated = ra;
        this.io = io;
        this.main = main;
    }


    public Observable<ViewStateHome> updateStream() {
        start();
        return state;
    }

    public void start() {
        if (home == null) {
            Observable<ViewStateHomePartial.ProgressivePopular> popular = Observable.just(new ViewStateHomePartial.ProgressivePopular());
            Observable<ViewStateHomePartial.ProgressivePopular> subject = PublishSubject.create();
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
                            });
            home = Observable.merge(videos, popular, subject)
                    .subscribeOn(io)
                    .observeOn(main)
                    .scan(ViewStateHome.builder().build(), new BiFunction<ViewStateHome, ViewStateHomePartial, ViewStateHome>() {
                        @Override
                        public ViewStateHome apply(@NonNull ViewStateHome viewStateHome, @NonNull ViewStateHomePartial vp) throws Exception {
                            return vp.reduce(viewStateHome);
                        }
                    });
            home.subscribe(this);
        }
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

    private static class VideoListObservableTransformer implements ObservableTransformer<Video, List<Video>> {
        @Override
        public ObservableSource<List<Video>> apply(Observable<Video> upstream) {
            return upstream.map(new Function<Video, List<Video>>() {
                @Override
                public List<Video> apply(@NonNull Video video) throws Exception {
                    ArrayList<Video> videos = new ArrayList<>(1);
                    videos.add(video);
                    return videos;
                }
            }).scan(new BiFunction<List<Video>, List<Video>, List<Video>>() {
                @Override
                public List<Video> apply(@NonNull List<Video> videos, @NonNull List<Video> videos2) throws Exception {
                    videos.addAll(videos2);
                    return videos;
                }
            });
        }
    }

}
