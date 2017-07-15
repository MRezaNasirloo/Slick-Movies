package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterSimilar;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.mapper.MovieDomainMovieMapper;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterSimilarImpl;
import com.github.pedramrn.slick.parent.ui.home.IdBank;
import com.github.pedramrn.slick.parent.ui.home.item.ItemView;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.util.ScanList;
import com.github.pedramrn.slick.parent.util.ScanToList;
import com.github.slick.SlickPresenter;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public class PresenterDetails extends SlickPresenter<ViewDetails> implements Observer<ViewStateDetails> {
    private static final String TAG = PresenterDetails.class.getSimpleName();
    private final RouterMovieDetails routerMovieDetails;
    private final RouterSimilar routerSimilar;
    private final MovieDomainMovieMapper mapper;
    private final MapperMovieSmallDomainMovieSmall mapperSmall;
    private final Scheduler io;
    private final Scheduler main;
    private BehaviorSubject<ViewStateDetails> state = BehaviorSubject.create();
    private Observable<ViewStateDetails> details;
    private Disposable disposable;
    private final String CASTS = "CASTS";
    private final String SIMILAR = "SIMILAR";
    private final String BACKDROPS = "BACKDROPS";

    @Inject
    public PresenterDetails(RouterMovieDetailsImpl rmd,
                            RouterSimilarImpl rs,
                            MovieDomainMovieMapper mapper,
                            MapperMovieSmallDomainMovieSmall mapperSmall,
                            @Named("io") Scheduler io,
                            @Named("main") Scheduler main
    ) {
        this.routerMovieDetails = rmd;
        this.routerSimilar = rs;
        this.mapper = mapper;
        this.mapperSmall = mapperSmall;
        this.io = io;
        this.main = main;
    }

    Observable<ViewStateDetails> updateStream() {
        return state;
    }

    void getMovieDetails(MovieBasic movieBasic) {
        if (details == null) {
            IdBank.reset(SIMILAR);
            IdBank.reset(CASTS);
            IdBank.reset(BACKDROPS);

            Observable<Movie> movieFull = routerMovieDetails.get(movieBasic.id())
                    //Maps the domains Models to View Models which have android dependency
                    .map(mapper)
                    .subscribeOn(io)
                    .share();

            Observable<PartialViewState<ViewStateDetails>> movie = movieFull
                    .map(new Function<Movie, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Movie movie) throws Exception {
                            return new PartialViewStateDetails.MovieFull(movie);
                        }
                    })
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorMovieFull(throwable);
                        }
                    });

            Observable<PartialViewState<ViewStateDetails>> backdrops = movieFull
                    .take(1)
                    .concatMap(new Function<Movie, ObservableSource<Backdrop>>() {
                        @Override
                        public ObservableSource<Backdrop> apply(@NonNull Movie movie) throws Exception {
                            return Observable.fromIterable(movie.images().backdrops());
                        }
                    })
                    .map(new MapProgressive())
                    .cast(ItemView.class)
                    .map(new Function<ItemView, Item>() {
                        @Override
                        public Item apply(@NonNull ItemView itemView) throws Exception {
                            return itemView.render(BACKDROPS);
                        }
                    })
                    .buffer(200)
                    .map(new Function<List<Item>, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull List<Item> items) throws Exception {
                            return new PartialViewStateDetails.MovieBackdrops(items);
                        }
                    })
                    .startWith(new PartialViewStateDetails.MovieBackdropsProgressive(5, BACKDROPS))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorMovieBackdrop(throwable);
                        }
                    });

            Observable<PartialViewState<ViewStateDetails>> casts = movieFull
                    .take(1)
                    .concatMap(new Function<Movie, ObservableSource<Cast>>() {
                        @Override
                        public ObservableSource<Cast> apply(@NonNull Movie movie) throws Exception {
                            return Observable.fromIterable(movie.casts());
                        }
                    })
                    .map(new MapProgressive())
                    .cast(ItemView.class)
                    .map(new Function<ItemView, Item>() {
                        @Override
                        public Item apply(@NonNull ItemView itemView) throws Exception {
                            return itemView.render(CASTS);
                        }
                    })
                    .buffer(200)
                    .map(new Function<List<Item>, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull List<Item> items) throws Exception {
                            return new PartialViewStateDetails.MovieCast(items);
                        }
                    })
                    .startWith(new PartialViewStateDetails.MovieCastsProgressive(5, CASTS))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorMovieCast(throwable);
                        }
                    });


            Observable<PartialViewState<ViewStateDetails>> similar = routerSimilar.similar(movieBasic.id(), 1)
                    .concatMap(new Function<List<MovieSmallDomain>, ObservableSource<MovieSmallDomain>>() {
                        @Override
                        public ObservableSource<MovieSmallDomain> apply(@NonNull List<MovieSmallDomain> movieSmallDomains) throws Exception {
                            return Observable.fromIterable(movieSmallDomains);
                        }
                    })
                    .map(mapperSmall)
                    .map(new MapProgressive())
                    .cast(ItemView.class)
                    .map(new Function<ItemView, Item>() {
                        @Override
                        public Item apply(@NonNull ItemView itemCard) throws Exception {
                            return itemCard.render(SIMILAR);
                        }
                    })
                    .buffer(20)
                    .map(new Function<List<Item>, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull List<Item> movies) throws Exception {
                            return new PartialViewStateDetails.Similar(movies);
                        }
                    })
                    .startWith(new PartialViewStateDetails.ItemProgressiveSimilar(5, SIMILAR))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorSimilar(throwable);
                        }
                    })
                    .subscribeOn(io);

            List<Observable<PartialViewState<ViewStateDetails>>> partials = new ArrayList<>(5);
            partials.add(movie);
            partials.add(casts);
            partials.add(backdrops);
            partials.add(similar);

            ViewStateDetails initial = ViewStateDetails.builder()
                    .casts(Collections.<Item>emptyList())
                    .backdrops(Collections.<Item>emptyList())
                    .similar(Collections.<Item>emptyList())
                    .movieBasic(movieBasic)
                    .build();

            details = Observable.merge(partials)
                    .observeOn(main)
                    .scan(initial, new BiFunction<ViewStateDetails, PartialViewState<ViewStateDetails>, ViewStateDetails>() {
                        @Override
                        public ViewStateDetails apply(@NonNull ViewStateDetails viewStateDetails, @NonNull PartialViewState<ViewStateDetails> partial)
                                throws Exception {
                            return partial.reduce(viewStateDetails);
                        }
                    })
            ;
        }
        details.subscribe(this);

    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(ViewStateDetails viewStateDetails) {
        state.onNext(viewStateDetails);

    }

    @Override
    public void onError(Throwable e) {
        // TODO: 2017-06-15 show error with view state
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) disposable.dispose();
        IdBank.dispose(SIMILAR);
        IdBank.dispose(CASTS);
        IdBank.dispose(BACKDROPS);
    }

}
