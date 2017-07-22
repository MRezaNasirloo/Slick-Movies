package com.github.pedramrn.slick.parent.ui;

import android.util.Log;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.slick.SlickPresenter;

import java.util.Arrays;

import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-22
 */

public class PresenterBase<V, S> extends SlickPresenter<V> implements Observer<S> {
    private static final String TAG = PresenterBase.class.getSimpleName();
    protected final Scheduler io;
    protected final Scheduler main;
    private final BehaviorSubject<S> state = BehaviorSubject.create();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private boolean hasSubscribed;

    public PresenterBase(@Named("main") Scheduler main, @Named("io") Scheduler io) {
        this.main = main;
        this.io = io;
    }

    public Observable<S> updateStream() {
        return state;
    }

    protected void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onDestroy() {
        dispose(disposable);
        super.onDestroy();
    }

    @Override
    public void onSubscribe(Disposable d) {
        hasSubscribed = true;
        disposable.add(d);

    }

    @Override
    public void onNext(S newState) {
        state.onNext(newState);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete() called");
    }

    protected Observable<S> reduce(S initialState, Observable<PartialViewState<S>> partialViewState) {
        return partialViewState
                .observeOn(main)
                .scan(initialState, new BiFunction<S, PartialViewState<S>, S>() {
                    @Override
                    public S apply(@NonNull S oldState, @NonNull PartialViewState<S> partialViewState1) throws Exception {
                        return partialViewState1.reduce(oldState);
                    }
                });
    }

    @SafeVarargs
    protected final Observable<PartialViewState<S>> merge(Observable<PartialViewState<S>>... partials) {
        return Observable.merge(Arrays.asList(partials));
    }

    public boolean hasSubscribed() {
        return hasSubscribed;
    }
}
