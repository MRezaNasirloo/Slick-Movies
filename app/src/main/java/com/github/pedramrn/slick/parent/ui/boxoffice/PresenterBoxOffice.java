package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;
import com.github.pedramrn.slick.parent.ui.boxoffice.router.RouterBoxOfficeImpl;
import com.github.slick.SlickPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */
public class PresenterBoxOffice extends SlickPresenter<ViewBoxOffice> implements Observer<List<MovieBoxOffice>> {
    private static final String TAG = PresenterBoxOffice.class.getSimpleName();

    private final RouterBoxOffice routerBoxOffice;
    private final Scheduler main;
    private final Scheduler io;
    private BehaviorSubject<ViewStateBoxOffice> state = BehaviorSubject.create();
    private final BehaviorSubject<Integer> triggerSubject = BehaviorSubject.create();

    private CompositeDisposable disposable = new CompositeDisposable();
    private Observable<List<MovieBoxOffice>> boxOffice;


    @Inject
    public PresenterBoxOffice(RouterBoxOfficeImpl routerBoxOffice, @Named("main") Scheduler main, @Named("io") Scheduler io) {
        this.routerBoxOffice = routerBoxOffice;
        this.main = main;
        this.io = io;
    }

    public void getBoxOffice(Observable<Integer> trigger, int pageSize) {
        trigger.subscribe(triggerSubject);
        if (boxOffice == null) {
            boxOffice = boxOffice(triggerSubject, pageSize).share();
            boxOffice.subscribe(this);
        }
    }

    public void pullToRefresh(Observable<Integer> trigger, int pageSize) {
        trigger.subscribe(triggerSubject);
        disposable.dispose();
        boxOffice = boxOffice(triggerSubject, pageSize).share();
        boxOffice.subscribe(this);
    }

    private Observable<List<MovieBoxOffice>> boxOffice(Observable<Integer> trigger, int pageSize) {
        return routerBoxOffice.boxOffice(trigger, pageSize)
                .map(new Function<MovieItem, MovieBoxOffice>() {
                    @Override
                    public MovieBoxOffice apply(@NonNull MovieItem movieItem) throws Exception {
                        return MovieBoxOffice.create(movieItem.name(), movieItem.revenue(), movieItem.poster(), movieItem.scoreMeta(), movieItem.scoreImdb(),
                                movieItem.votesImdb(), movieItem.rated(), movieItem.runtime(), movieItem.genre(), movieItem.director(),
                                movieItem.writer(), movieItem.actors(), movieItem.plot(), movieItem.production(), movieItem.released(),
                                movieItem.imdb(), movieItem.trakt(), movieItem.tmdb());
                    }
                }).map(new Function<MovieBoxOffice, List<MovieBoxOffice>>() {
                    @Override
                    public List<MovieBoxOffice> apply(@NonNull MovieBoxOffice movieBoxOfficeItem) throws Exception {
                        final ArrayList<MovieBoxOffice> list = new ArrayList<>();
                        list.add(movieBoxOfficeItem);
                        return list;
                    }
                }).scan(new BiFunction<List<MovieBoxOffice>, List<MovieBoxOffice>, List<MovieBoxOffice>>() {
                    @Override
                    public List<MovieBoxOffice> apply(@NonNull List<MovieBoxOffice> movieBoxOfficeItems, @NonNull List<MovieBoxOffice> movieBoxOfficeItems2) throws Exception {
                        movieBoxOfficeItems.addAll(movieBoxOfficeItems2);
                        return movieBoxOfficeItems;
                    }
                }).subscribeOn(io)
                .observeOn(main);
    }

    public Observable<ViewStateBoxOffice> updateStream() {
        return state;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable.add(d);

    }

    @Override
    public void onNext(List<MovieBoxOffice> movieBoxOfficeItems) {
        state.onNext(ViewStateBoxOffice.create(new ArrayList<>(movieBoxOfficeItems)));

    }

    @Override
    public void onError(Throwable e) {
        state.onError(e);
    }

    @Override
    public void onComplete() {
        // We don't want to terminate the update stream.
        //state.onComplete();
    }
}
