package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.domain.model.MovieItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class ViewModelBoxOffice implements Observer<ViewStateBoxOffice> {
    private final PresenterBoxOffice presenter;
    private Observable<ViewStateBoxOffice> viewStateBoxOffice;
    private final ViewBoxOffice viewBoxOffice;
    private BehaviorSubject<List<MovieItem>> movieItems = BehaviorSubject.create();


    public ViewModelBoxOffice(PresenterBoxOffice presenter, ViewBoxOffice viewBoxOffice) {
        this.presenter = presenter;
        this.viewStateBoxOffice = presenter.updateStream();
        this.viewBoxOffice = viewBoxOffice;
        this.viewStateBoxOffice.subscribe(this);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ViewStateBoxOffice viewStateBoxOffice) {
        viewBoxOffice.render(viewStateBoxOffice);
        movieItems.onNext(viewStateBoxOffice.movieItems());
    }

    @Override
    public void onError(Throwable e) {
//        viewBoxOffice.onError(e);
    }

    @Override
    public void onComplete() {
//        viewBoxOffice.onComplete();
    }

    public void listTrigger(Observable<Integer> trigger, int i) {
        presenter.getBoxOffice(trigger, 2);
    }

    public Disposable subscribeToBoxOfficeList(RecyclerViewBoxOffice recyclerViewBoxOffice) {
        return movieItems.subscribe(recyclerViewBoxOffice);
    }
}
