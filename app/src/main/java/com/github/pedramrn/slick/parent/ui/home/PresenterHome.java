package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterPopular;
import com.github.pedramrn.slick.parent.domain.router.RouterTrending;
import com.github.pedramrn.slick.parent.domain.router.RouterUpcoming;
import com.github.pedramrn.slick.parent.domain.rx.OnCompleteReturn;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterPopularImpl;
import com.github.pedramrn.slick.parent.ui.home.router.RouterTrendingImpl;
import com.github.pedramrn.slick.parent.ui.home.router.RouterUpcomingImpl;
import com.github.pedramrn.slick.parent.ui.home.state.PartialViewStateHome;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.util.ScanToMap;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class PresenterHome extends PresenterBase<ViewHome, ViewStateHome> {

    public static final String TRENDING = "TRENDING";
    public static final String POPULAR = "POPULAR";
    public static final String BANNER = "BANNER";

    private final RouterTrending routerTrending;
    private final RouterUpcoming routerUpcoming;
    private final RouterPopular routerPopular;

    private final MapperMovieMetadataToMovieBasic mapperMetadata;
    private final MapperMovieSmallDomainMovieSmall mapperMovieSmall;

    private final MapProgressive mapProgressiveTrending = new MapProgressive();
    private final MapProgressive mapProgressivePopular = new MapProgressive();

    private final ScanToMap<Item> scanToMap = new ScanToMap<>();

    @Inject
    public PresenterHome(
            RouterTrendingImpl routerTrending,
            RouterPopularImpl routerPopular,
            RouterUpcomingImpl routerUpcoming,
            MapperMovieSmallDomainMovieSmall mapperMovieSmall,
            MapperMovieMetadataToMovieBasic mapperMetadata,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main
    ) {
        super(main, io);
        this.routerTrending = routerTrending;
        this.routerUpcoming = routerUpcoming;
        this.routerPopular = routerPopular;
        this.mapperMovieSmall = mapperMovieSmall;
        this.mapperMetadata = mapperMetadata;
    }

    @Override
    public void start(ViewHome view) {
        System.out.println("PresenterHome.start");
        final int pageSize = view.pageSize();

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
                                return movieSmall.render(BANNER);
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
                        .startWith(new PartialViewStateHome.ProgressiveBannerImpl(2, BANNER))
                        .subscribeOn(io);
            }
        });

        // TODO: 2017-07-08 extract a transformer

        Observable<Integer> triggerTrending = command(new CommandProvider<Integer, ViewHome>() {
            @Override
            public Observable<Integer> provide(ViewHome view) {
                return view.triggerTrending();
            }
        });
        Observable<PartialViewState<ViewStateHome>> trending = triggerTrending.startWith(1).concatMap(new Function<Integer,
                ObservableSource<PartialViewState<ViewStateHome>>>() {
            @Override
            public ObservableSource<PartialViewState<ViewStateHome>> apply(@NonNull final Integer page) throws Exception {
                System.out.println("PresenterHome.trending page " + page);
                return routerTrending.trending(page, pageSize).subscribeOn(io)
                        .map(mapperMetadata)
                        .cast(AutoBase.class)
                        .map(mapProgressiveTrending)
                        .cast(ItemView.class)
                        .map(new Function<ItemView, Item>() {
                            @Override
                            public Item apply(@NonNull ItemView itemView) throws Exception {
                                return itemView.render(TRENDING);
                            }
                        })
                        .compose(scanToMap)
                        .map(new Function<Map<Integer, Item>, PartialViewState<ViewStateHome>>() {
                            @Override
                            public PartialViewState<ViewStateHome> apply(@NonNull Map<Integer, Item> items) throws Exception {
                                System.out.println("PresenterHome.Trending size: " + items.size());
                                return new PartialViewStateHome.Trending(new LinkedHashMap<>(items));
                            }
                        })
                        .lift(new OnCompleteReturn<PartialViewState<ViewStateHome>>() {
                            @Override
                            public PartialViewState<ViewStateHome> apply(@NonNull Boolean hadError) throws Exception {
                                System.out.println("PresenterHome.TrendingLoaded had Error: " + hadError);
                                return new PartialViewStateHome.TrendingLoaded(!hadError);
                            }
                        })
                        .startWith(new PartialViewStateHome.CardProgressiveTrending(pageSize, TRENDING))
                        .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateHome>>() {
                                           @Override
                                           public PartialViewState<ViewStateHome> apply(@NonNull Throwable throwable)
                                                   throws Exception {
                                               System.out.println("PresenterHome.ErrorTrending");
                                               return new PartialViewStateHome.ErrorTrending(throwable);
                                           }
                                       }
                        );
            }
        });

        // TODO: 2017-07-08 extract a transformer
        Observable<PartialViewState<ViewStateHome>> trendingProgressiveLoading = triggerTrending
                .map(new Function<Integer, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull Integer integer) throws Exception {
                        return new PartialViewStateHome.CardProgressiveTrending(pageSize, TRENDING);
                    }
                });

        Observable<Integer> triggerPopular = command(new CommandProvider<Integer, ViewHome>() {
            @Override
            public Observable<Integer> provide(ViewHome view) {
                return view.triggerPopular();
            }
        });
        Observable<PartialViewState<ViewStateHome>> popular = triggerPopular.startWith(1)
                .concatMap(new Function<Integer, ObservableSource<PartialViewState<ViewStateHome>>>() {
                    @Override
                    public ObservableSource<PartialViewState<ViewStateHome>> apply(@NonNull Integer page) throws Exception {
                        return routerPopular.popular(page, pageSize).subscribeOn(io)
                                .map(mapperMetadata)
                                .cast(AutoBase.class)
                                .map(mapProgressivePopular)
                                .cast(ItemView.class)
                                .map(new Function<ItemView, Item>() {
                                    @Override
                                    public Item apply(@NonNull ItemView itemView) throws Exception {
                                        return itemView.render(POPULAR);
                                    }
                                })
                                .compose(scanToMap)
                                .map(new Function<Map<Integer, Item>, PartialViewState<ViewStateHome>>() {
                                    @Override
                                    public PartialViewState<ViewStateHome> apply(@NonNull Map<Integer, Item> items) throws Exception {
                                        System.out.println("PresenterHome.Popular");
                                        return new PartialViewStateHome.Popular(items);
                                    }
                                })
                                .lift(new OnCompleteReturn<PartialViewState<ViewStateHome>>() {
                                    @Override
                                    public PartialViewState<ViewStateHome> apply(@NonNull Boolean hadError) throws Exception {
                                        System.out.println("PresenterHome.PopularLoaded had Error: " + hadError);
                                        return new PartialViewStateHome.PopularLoaded(!hadError);
                                    }
                                })
                                .startWith(new PartialViewStateHome.CardProgressivePopular(pageSize, POPULAR))
                                .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateHome>>() {
                                                   @Override
                                                   public PartialViewState<ViewStateHome> apply(@NonNull Throwable throwable)
                                                           throws Exception {
                                                       System.out.println("PresenterHome.ErrorPopular");
                                                       return new PartialViewStateHome.ErrorPopular(throwable);
                                                   }
                                               }
                                ).doOnComplete(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        System.out.println("PresenterHome.doOnComplete");
                                    }
                                })
                                ;
                    }
                });

        Observable<PartialViewState<ViewStateHome>> popularProgressiveLoading = triggerPopular
                .map(new Function<Integer, PartialViewState<ViewStateHome>>() {
                    @Override
                    public PartialViewState<ViewStateHome> apply(@NonNull Integer integer) throws Exception {
                        return new PartialViewStateHome.CardProgressivePopular(pageSize, POPULAR);
                    }
                });


        ViewStateHome initialState = ViewStateHome.builder()
                .upcoming(Collections.<Item>emptyList())
                .popular(Collections.<Integer, Item>emptyMap())
                .trending(Collections.<Integer, Item>emptyMap())
                .loadingTrending(true)
                .loadingPopular(true)
                .itemLoadingCountPopular(0)
                .itemLoadingCountTrending(0)
                .pagePopular(1)
                .pageTrending(1)
                .build();

        reduce(initialState, merge(upcoming, trending, trendingProgressiveLoading, popular, popularProgressiveLoading))
                .subscribe(this);
    }

    @Override
    public void render(@NonNull ViewStateHome state, @NonNull ViewHome view) {
        System.out.println("PresenterHome.render");
    }
}
