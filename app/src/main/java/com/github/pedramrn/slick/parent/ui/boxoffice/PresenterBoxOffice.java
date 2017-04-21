package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
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
public class PresenterBoxOffice extends SlickPresenter<ViewBoxOffice> implements Observer<List<MovieItem>> {
    private static final String TAG = PresenterBoxOffice.class.getSimpleName();

    private final RouterBoxOffice routerBoxOffice;
    private final Scheduler main;
    private final Scheduler io;
    private BehaviorSubject<ViewStateBoxOffice> state = BehaviorSubject.create();
    private final BehaviorSubject<Integer> triggerSubject = BehaviorSubject.create();

    private CompositeDisposable disposable = new CompositeDisposable();
    private Observable<List<MovieItem>> boxOffice;


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

    private Observable<List<MovieItem>> boxOffice(Observable<Integer> trigger, int pageSize) {
        return routerBoxOffice.boxOffice(trigger, pageSize)
                .map(new Function<MovieItem, List<MovieItem>>() {
                    @Override
                    public List<MovieItem> apply(@NonNull MovieItem movieItem) throws Exception {
                        final ArrayList<MovieItem> list = new ArrayList<>();
                        list.add(movieItem);
                        return list;
                    }
                }).scan(new BiFunction<List<MovieItem>, List<MovieItem>, List<MovieItem>>() {
                    @Override
                    public List<MovieItem> apply(@NonNull List<MovieItem> movieItems, @NonNull List<MovieItem> movieItems2) throws Exception {
                        movieItems.addAll(movieItems2);
                        return movieItems;
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
    public void onNext(List<MovieItem> movieItems) {
        state.onNext(ViewStateBoxOffice.create(new ArrayList<>(movieItems)));

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
