package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterUpcoming;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterUpcomingImpl;
import com.github.pedramrn.slick.parent.ui.home.state.PartialViewStateHome;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class PresenterHome extends PresenterBase<ViewHome, ViewStateHome> {

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
    public void start(ViewHome view) {
        System.out.println("PresenterHome.start");

        Observable<PartialViewState<ViewStateHome>> upcoming = command(new CommandProvider<Object, ViewHome>() {
            @Override
            public Observable<Object> provide(ViewHome view) {
                return view.retryUpcoming();
            }
        }).startWith(this).flatMap(new Function<Object, ObservableSource<PartialViewState<ViewStateHome>>>() {
            @Override
            public ObservableSource<PartialViewState<ViewStateHome>> apply(@NonNull Object o) throws Exception {
                System.out.println("PresenterHome.routerUpcoming(1)");
                return routerUpcoming.upcoming(1)
                        .concatMap(new Function<PagedDomain<MovieSmallDomain>, ObservableSource<MovieSmallDomain>>() {
                            @Override
                            public ObservableSource<MovieSmallDomain> apply(@NonNull PagedDomain<MovieSmallDomain> pagedData)
                                    throws Exception {
                                return Observable.fromIterable(pagedData.data());
                            }
                        })
                        .map(mapperMovieSmall)
                        .map(new MapProgressive())
                        .cast(MovieSmall.class)
                        .map(new Function<MovieSmall, Item>() {
                            @Override
                            public Item apply(@NonNull MovieSmall movieSmall)
                                    throws Exception {
                                return movieSmall.render(UPCOMING);
                            }
                        })
                        .buffer(20)
                        .map(new Function<List<Item>, PartialViewState<ViewStateHome>>() {
                            @Override
                            public PartialViewState<ViewStateHome> apply(@NonNull List<Item> items) throws Exception {
                                System.out.println("PresenterHome.Upcoming");
                                return new PartialViewStateHome.Upcoming(items);
                            }
                        })
                        .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateHome>>() {
                            @Override
                            public PartialViewState<ViewStateHome> apply(@NonNull Throwable throwable) throws Exception {
                                System.out.println("PresenterHome.UpcomingError");
                                return new PartialViewStateHome.UpcomingError(throwable);
                            }
                        })
                        .startWith(new PartialViewStateHome.ProgressiveBannerImpl(2, UPCOMING))
                        .subscribeOn(io);
            }
        });


        ViewStateHome initialState = ViewStateHome.builder()
                .upcoming(Collections.<Item>emptyList())
                .build();

        reduce(initialState, merge(upcoming))
                .subscribe(this);
    }

    @Override
    public void render(@NonNull ViewStateHome state, @NonNull ViewHome view) {
        System.out.println("PresenterHome.render");
    }
}
