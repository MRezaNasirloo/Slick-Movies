package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterSimilar;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.mapper.MovieDomainMovieMapper;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterSimilarImpl;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.util.ScanToList;
import com.github.slick.SlickPresenter;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    void getMovieDetails(Integer tmdbId) {
        if (details == null) {
            /*details = routerMovieDetails.get(tmdbId)
                    //Maps the domains Models to View Models which have android dependency
                    .map(mapper)
                    .subscribeOn(io)
                    .observeOn(main);
            details.subscribe(new Consumer<Movie>() {
                @Override
                public void accept(@NonNull Movie movie) throws Exception {

                }
            });*/

            Observable<PartialViewState<ViewStateDetails>> similar = routerSimilar.similar(tmdbId, 1)
                    .flatMap(new Function<List<MovieSmallDomain>, ObservableSource<MovieSmallDomain>>() {
                        @Override
                        public ObservableSource<MovieSmallDomain> apply(@NonNull List<MovieSmallDomain> movieSmallDomains) throws Exception {
                            return Observable.fromIterable(movieSmallDomains);
                        }
                    })
                    .map(mapperSmall)
                    .map(new MapProgressive())
                    .cast(ItemCard.class)
                    .map(new Function<ItemCard, Item>() {
                        @Override
                        public Item apply(@NonNull ItemCard itemCard) throws Exception {
                            return itemCard.render("SIMILAR");
                        }
                    })
                    .compose(new ScanToList<Item>())
                    .map(new Function<List<Item>, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull List<Item> movies) throws Exception {
                            return new PartialViewStateDetails.Similar(movies);
                        }
                    })
                    .startWith(new PartialViewStateDetails.CardProgressiveSimilar(10, "SIMILAR"))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorSimilar(throwable);
                        }
                    })
                    .subscribeOn(io);

            List<Observable<PartialViewState<ViewStateDetails>>> partials = new ArrayList<>(5);
            partials.add(Observable.<PartialViewState<ViewStateDetails>>never());
            partials.add(similar);

            ViewStateDetails initial = ViewStateDetails.builder()
                    .similar(Collections.<Item>emptyList())
                    .movie(null)
                    .build();

            details = Observable.merge(partials)
                    .observeOn(main)
                    .scan(initial, new BiFunction<ViewStateDetails, PartialViewState<ViewStateDetails>, ViewStateDetails>() {
                        @Override
                        public ViewStateDetails apply(@NonNull ViewStateDetails viewStateDetails, @NonNull PartialViewState<ViewStateDetails> partial)
                                throws Exception {
                            return partial.reduce(viewStateDetails);
                        }
                    });
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
        // state.onNext(new ViewStateDetailsError(e));
        // TODO: 2017-06-15 show error with view state
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // if (disposable != null) disposable.dispose();
    }

}
