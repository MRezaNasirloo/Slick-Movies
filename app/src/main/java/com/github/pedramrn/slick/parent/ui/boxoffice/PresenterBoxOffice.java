package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.ui.boxoffice.router.RouterBoxOfficeImpl;
import com.github.slick.SlickPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */
public class PresenterBoxOffice extends SlickPresenter<ViewBoxOffice> {
    private static final String TAG = PresenterBoxOffice.class.getSimpleName();

    private final RouterBoxOffice routerBoxOffice;
    private BehaviorSubject<ViewStateBoxOffice> state = BehaviorSubject.create();
    private final BehaviorSubject<Integer> trigger = BehaviorSubject.create();

    private CompositeDisposable disposable = new CompositeDisposable();
    private boolean hasSubscribe = false;


    @Inject
    public PresenterBoxOffice(RouterBoxOfficeImpl routerBoxOffice) {
        this.routerBoxOffice = routerBoxOffice;
    }

    public void getBoxOffice() {
        routerBoxOffice.boxOffice(trigger, 2)
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
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MovieItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);

                    }

                    @Override
                    public void onNext(List<MovieItem> movieItems) {
                        state.onNext(ViewStateBoxOffice.create(movieItems));
                    }

                    @Override
                    public void onError(Throwable e) {
                        state.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //                        state.onComplete();
                    }
                });
    }

    public Observable<ViewStateBoxOffice> updateStream() {
        return state;
    }

    public void loadMore() {
        trigger.onNext(0);
        if (!hasSubscribe) {
            hasSubscribe = true;
            getBoxOffice();

        }
    }
}
