package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.MovieItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class ViewModelBoxOffice implements Observer<ViewStateBoxOffice> {
    private static final String TAG = ViewModelBoxOffice.class.getSimpleName();
    private final CompositeDisposable disposable;
    private final PresenterBoxOffice presenter;
    private final ViewBoxOffice view;
    private BehaviorSubject<List<MovieItem>> movieItems = BehaviorSubject.create();


    public ViewModelBoxOffice(CompositeDisposable disposable, PresenterBoxOffice presenter, ViewBoxOffice view) {
        this.view = view;
        this.presenter = presenter;
        this.disposable = disposable;
        presenter.updateStream().subscribe(this);
    }

    public void adapterUpdateCommand(Observable<Integer> observable) {
        disposable.add(observable.subscribe());
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable.add(d);
    }

    @Override
    public void onNext(ViewStateBoxOffice viewStateBoxOffice) {
        view.render(viewStateBoxOffice);
        movieItems.onNext(viewStateBoxOffice.movieItems());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //        view.onError(e);
    }

    @Override
    public void onComplete() {
        //        view.onComplete();
        Log.d(TAG, "onComplete() called");
    }

    public void onLoadMoreTrigger(Observable<Integer> trigger, int page) {
        presenter.getBoxOffice(trigger, page);
    }

    public void boxOfficeList(AdapterBoxOffice adapterBoxOffice) {
        movieItems.subscribe(adapterBoxOffice);
    }
}
