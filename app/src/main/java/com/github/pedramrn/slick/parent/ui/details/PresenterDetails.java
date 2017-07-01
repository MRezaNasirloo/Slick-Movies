package com.github.pedramrn.slick.parent.ui.details;

import android.util.Log;

import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.ui.details.mapper.MovieDomainMovieMapper;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsImpl;
import com.github.slick.SlickPresenter;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public class PresenterDetails extends SlickPresenter<ViewDetails> implements Observer<Movie> {
    private static final String TAG = PresenterDetails.class.getSimpleName();
    private final RouterMovieDetails routerMovieDetails;
    private final Scheduler io;
    private final Scheduler main;
    private BehaviorSubject<ViewStateDetails> state = BehaviorSubject.create();
    private Observable<Movie> details;
    private Disposable disposable;

    @Inject
    public PresenterDetails(RouterMovieDetailsImpl routerMovieDetails, @Named("io") Scheduler io, @Named("main") Scheduler main) {
        this.routerMovieDetails = routerMovieDetails;
        this.io = io;
        this.main = main;
    }

    Observable<ViewStateDetails> updateStream() {
        return state;
    }

    void getMovieDetails(Integer tmdbId) {
        if (details == null) {
            details = routerMovieDetails.get(tmdbId)
                    //Maps the domains Models to View Models which have android dependency
                    .map(new MovieDomainMovieMapper()).subscribeOn(io).observeOn(main);
            details.subscribe(this);
        }

    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(Movie movie) {
        Log.d(TAG, "onNext() called");
        state.onNext(new ViewStateDetails(movie));
    }

    @Override
    public void onError(Throwable e) {
        // state.onNext(new ViewStateDetailsError(e));
        // TODO: 2017-06-15 show error with view state
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) disposable.dispose();
    }

}
