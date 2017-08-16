package com.github.pedramrn.slick.parent.ui.search;

import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterSearch;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.search.item.ItemRowSuggestion;
import com.github.pedramrn.slick.parent.ui.search.router.RouterSearchImpl;
import com.github.pedramrn.slick.parent.ui.search.state.PartialViewStateSearch;
import com.github.pedramrn.slick.parent.ui.search.state.ViewStateSearch;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * A simple Presenter
 */
public class PresenterSearch extends PresenterBase<ViewSearch, ViewStateSearch> {

    private final RouterSearch routerSearch;
    private final MapperMovieSmallDomainMovieSmall mapperSmall;

    @Inject
    public PresenterSearch(RouterSearchImpl routerSearch,
                           MapperMovieSmallDomainMovieSmall mapperSmall,
                           @Named("main") Scheduler main,
                           @Named("io") Scheduler io) {
        super(main, io);
        this.routerSearch = routerSearch;
        this.mapperSmall = mapperSmall;
    }

    public void query(Observable<String> queryNewText, Observable<Boolean> openClose) {
        if (!hasSubscribed()) {
            Observable<PartialViewState<ViewStateSearch>> searchMovies = queryNewText
                    .flatMap(new Function<String, ObservableSource<PagedDomain<MovieSmallDomain>>>() {
                        @Override
                        public ObservableSource<PagedDomain<MovieSmallDomain>> apply(@NonNull String queryText) throws Exception {
                            return routerSearch.movies(queryText, 1).subscribeOn(io);
                        }
                    })
                    .flatMap(new Function<PagedDomain<MovieSmallDomain>, ObservableSource<MovieSmallDomain>>() {
                        @Override
                        public ObservableSource<MovieSmallDomain> apply(@NonNull PagedDomain<MovieSmallDomain> pagedDomain) throws Exception {
                            return Observable.fromIterable(pagedDomain.data()).take(6);
                        }
                    })
                    .map(mapperSmall)
                    .map(new Function<MovieSmall, Item>() {
                        @Override
                        public Item apply(@NonNull MovieSmall movieSmall) throws Exception {
                            return new ItemRowSuggestion(movieSmall);
                        }
                    })
                    .buffer(6)
                    .map(new Function<List<Item>, PartialViewState<ViewStateSearch>>() {
                        @Override
                        public PartialViewState<ViewStateSearch> apply(@NonNull List<Item> items) throws Exception {
                            return new PartialViewStateSearch.Movies(items);
                        }
                    })
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateSearch>>() {
                        @Override
                        public PartialViewState<ViewStateSearch> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateSearch.ErrorMovie(throwable);
                        }
                    })
                    .subscribeOn(io);

            Observable<PartialViewState<ViewStateSearch>> searchOpenClose = openClose.map(new Function<Boolean, PartialViewState<ViewStateSearch>>() {
                @Override
                public PartialViewState<ViewStateSearch> apply(@NonNull Boolean isOpen) throws Exception {
                    return new PartialViewStateSearch.SearchOpenClose(isOpen);
                }
            });


            ViewStateSearch initial = ViewStateSearch.builder()
                    .movies(Collections.<Item>emptyList())
                    .loadingMovies(false)
                    .errorMovies(null)
                    .build();

            reduce(initial, merge(searchMovies, searchOpenClose)).subscribe(this);

        }
    }
}
