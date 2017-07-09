package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.ui.boxoffice.router.RouterBoxOfficeTmdbImpl;
import com.github.pedramrn.slick.parent.ui.details.mapper.MovieDomainMovieMapper;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
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
public class PresenterBoxOffice extends SlickPresenter<ViewBoxOffice> implements Observer<List<Movie>> {
    private static final String TAG = PresenterBoxOffice.class.getSimpleName();

    private final RouterBoxOffice routerBoxOffice;
    private final MovieDomainMovieMapper mapper;
    private final Scheduler main;
    private final Scheduler io;
    private BehaviorSubject<ViewStateBoxOffice> state = BehaviorSubject.create();
    private final BehaviorSubject<Integer> triggerSubject = BehaviorSubject.create();

    private CompositeDisposable disposable = new CompositeDisposable();
    private Observable<List<Movie>> boxOffice;


    @Inject
    public PresenterBoxOffice(RouterBoxOfficeTmdbImpl routerBoxOffice, MovieDomainMovieMapper mapper,
                              @Named("main") Scheduler main, @Named("io") Scheduler io) {
        this.routerBoxOffice = routerBoxOffice;
        this.mapper = mapper;
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

    private Observable<List<Movie>> boxOffice(Observable<Integer> trigger, int pageSize) {
        return routerBoxOffice.boxOffice(trigger, pageSize)
                .map(mapper)
                .map(new Function<Movie, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@NonNull Movie movieBoxOfficeItem) throws Exception {
                        final ArrayList<Movie> list = new ArrayList<>(1);
                        list.add(movieBoxOfficeItem);
                        return list;
                    }
                }).scan(new BiFunction<List<Movie>, List<Movie>, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@NonNull List<Movie> boxOfficeList, @NonNull List<Movie> boxOfficeList2)
                            throws Exception {
                        boxOfficeList.addAll(boxOfficeList2);
                        return boxOfficeList;
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
    public void onNext(List<Movie> movieBoxOfficeItems) {
        state.onNext(ViewStateBoxOffice.create(new ArrayList<>(movieBoxOfficeItems)));

    }

    @Override
    public void onError(Throwable e) {
        state.onError(e);
    }

    @Override
    public void onComplete() {
        // We don't want to terminate the update stream.
        //state.onCompleteGlide();
    }
}
