package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.ui.main.router.RouterBoxOfficeImpl;
import com.github.slick.SlickPresenter;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */
public class HomePresenter extends SlickPresenter<HomeView> {
    private static final String TAG = HomePresenter.class.getSimpleName();
    private final RouterBoxOffice routerBoxOffice;


    private BehaviorSubject<HomeViewState> state = BehaviorSubject.create();
    private Subscription s;


    @Inject
    public HomePresenter(RouterBoxOfficeImpl routerBoxOffice) {
        this.routerBoxOffice = routerBoxOffice;
    }

    public void getBoxOffice() {

    }

    public void getMore(Observable<Object> observable) {
        routerBoxOffice.getStream(observable)
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
                .subscribe(new Consumer<List<MovieItem>>() {
                    @Override
                    public void accept(@NonNull List<MovieItem> movieItems) throws Exception {
                        state.onNext(HomeViewState.create(movieItems));
                    }
                });
    }

    public BehaviorSubject<HomeViewState> updateStream() {
        return state;
    }

    public void navigate() {

    }
}
