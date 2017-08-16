package com.github.pedramrn.slick.parent.ui;

import android.support.annotation.CallSuper;
import android.support.v4.util.ArrayMap;

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
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-22
 */

public abstract class PresenterBase<V, S> extends SlickPresenter<V> implements Observer<S> {
    private static final String TAG = PresenterBase.class.getSimpleName();
    protected final Scheduler io;
    protected final Scheduler main;
    private final BehaviorSubject<S> state = BehaviorSubject.create();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private CompositeDisposable disposableIntents;
    private final ArrayMap<IntentProvider, PublishSubject> intents = new ArrayMap<>(3);
    private boolean hasSubscribed;

    public PresenterBase(@Named("main") Scheduler main, @Named("io") Scheduler io) {
        this.main = main;
        this.io = io;
    }

    @CallSuper
    @Override
    public void onViewUp(V view) {
        super.onViewUp(view);
        if (!hasSubscribed()) start();
        subscribeIntents(view);
    }

    @CallSuper
    @Override
    public void onViewDown() {
        super.onViewDown();
        dispose(disposableIntents);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        dispose(disposable);
        intents.clear();
        super.onDestroy();
    }

    /**
     * Only called once during the presenter's lifecycle
     */
    public abstract void start();

    public Observable<S> updateStream() {
        return state;
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
        System.out.println(TAG + " onComplete() called");
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

    protected <T> Observable<T> intent(IntentProvider<T, V> intent) {
        PublishSubject<T> publishSubject = PublishSubject.create();
        intents.put(intent, publishSubject);
        return publishSubject;
    }

    @SuppressWarnings("unchecked")
    private void subscribeIntents(V view) {
        if (disposableIntents == null || disposableIntents.isDisposed()) {
            disposableIntents = new CompositeDisposable();
        }
        for (IntentProvider intentProvider : intents.keySet()) {
            Observable<Object> observable = intentProvider.provide(view);
            final PublishSubject<Object> subject = intents.get(intentProvider);
            disposableIntents.add(observable.subscribeWith(new DisposableObserver() {
                @Override
                public void onNext(Object o) {
                    subject.onNext(o);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    //no-op
                }

                @Override
                public void onComplete() {
                    System.out.println("Intent completed!");
                    //no-op
                }
            }));
        }
    }

    protected interface IntentProvider<T, V> {
        Observable<T> provide(V view);
    }

    protected boolean hasSubscribed() {
        return hasSubscribed;
    }


    protected void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
