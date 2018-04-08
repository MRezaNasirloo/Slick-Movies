package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pedramrn.slick.parent.domain.router.RouterUpcoming;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterUpcomingImpl;
import com.github.pedramrn.slick.parent.ui.home.state.PartialViewStateHome;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-20
 */

public class PresenterHome extends SlickPresenterUni<ViewHome, ViewStateHome> {
    private static final String TAG = PresenterHome.class.getSimpleName();
    public static final String UPCOMING = "UPCOMING";

    private final RouterUpcoming routerUpcoming;

    private final MapperMovieSmallDomainMovieSmall mapperMovieSmall;

    @Inject
    public PresenterHome(
            RouterUpcomingImpl routerUpcoming,
            MapperMovieSmallDomainMovieSmall mapperMovieSmall,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main
    ) {
        super(main, io);
        this.routerUpcoming = routerUpcoming;
        this.mapperMovieSmall = mapperMovieSmall;
    }

    @Override
    public void onViewUp(ViewHome view) {
        super.onViewUp(view);
        Log.i(TAG, "onViewUp: called");
    }

    @Override
    public void onViewDown() {
        super.onViewDown();
        Log.i(TAG, "onViewDown called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: called");
    }

    @Override
    public void start(ViewHome view) {
        Observable<PartialViewState<ViewStateHome>> upcoming = command(ViewHome::retryUpcoming)
                .startWith(this).flatMap(o -> routerUpcoming.upcoming(1)
                        .concatMap(pagedData -> Observable.fromIterable(pagedData.data()))
                        .map(mapperMovieSmall)
                        .map(new MapProgressive())
                        .cast(MovieSmall.class)
                        .map(movieSmall -> movieSmall.render(UPCOMING))
                        .buffer(20)
                        .map((Function<List<Item>, PartialViewState<ViewStateHome>>) PartialViewStateHome.Upcoming::new)
                        .onErrorReturn(PartialViewStateHome.UpcomingError::new)
                        .startWith(new PartialViewStateHome.ProgressiveBannerImpl(2, UPCOMING))
                        .subscribeOn(io));


        ViewStateHome initialState = ViewStateHome.builder()
                .upcoming(Collections.emptyList())
                .build();

        subscribe(initialState, upcoming);
    }

    @Override
    public void render(@NonNull ViewStateHome state, @NonNull ViewHome view) {
        view.render(state);
    }
}
