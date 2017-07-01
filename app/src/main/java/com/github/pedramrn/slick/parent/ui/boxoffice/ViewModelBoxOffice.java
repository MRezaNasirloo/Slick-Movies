package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;

import java.net.HttpURLConnection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.HttpException;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class ViewModelBoxOffice implements Observer<ViewStateBoxOffice> {
    private static final String TAG = ViewModelBoxOffice.class.getSimpleName();
    private final CompositeDisposable disposable;
    private final PresenterBoxOffice presenter;
    private final ViewBoxOffice view;
    private BehaviorSubject<List<MovieBoxOffice>> movieItems = BehaviorSubject.create();
    private boolean isLoading = false;


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
        isLoading = false;
        view.render(viewStateBoxOffice);
        movieItems.onNext(viewStateBoxOffice.movieItems());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            if (((HttpException) e).code() == HttpURLConnection.HTTP_FORBIDDEN) {
                view.showSnackBar("Network field. Reset your router, ip is blocked.");
                return;
            }
        }
        // throw new RuntimeException(e);
        //        view.onError(e);
    }

    @Override
    public void onComplete() {
        //        view.onComplete();
    }

    public void pagination(Observable<Integer> trigger, int page) {
        presenter.getBoxOffice(trigger, page);
    }

    public Observable<List<MovieBoxOffice>> boxOfficeList() {
        return movieItems;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading() {
        isLoading = true;
    }
}
