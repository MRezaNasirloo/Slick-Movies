package com.github.pedramrn.slick.parent.ui.search;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.pedramrn.slick.parent.BuildConfig;
import com.github.pedramrn.slick.parent.domain.router.RouterSearch;
import com.github.pedramrn.slick.parent.domain.rx.OnCompleteReturn;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.search.item.ItemRowSuggestion;
import com.github.pedramrn.slick.parent.ui.search.router.RouterSearchImpl;
import com.github.pedramrn.slick.parent.ui.search.state.PartialViewStateSearch;
import com.github.pedramrn.slick.parent.ui.search.state.ViewStateSearch;
import com.xwray.groupie.Item;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
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

    @Override
    public void onViewUp(ViewSearch view) {
        super.onViewUp(view);
    }

    private static final String TAG = PresenterSearch.class.getSimpleName();

    @Override
    public void start(ViewSearch view) {
        Observable<PartialViewState<ViewStateSearch>> searchMovies = command(ViewSearch::queryNexText)
                .flatMap(queryText -> routerSearch.movies(queryText, 1)
                        .flatMap(pagedDomain -> Observable.fromIterable(pagedDomain.data()))
                        .map(mapperSmall)
                        .map((Function<MovieSmall, Item>) ItemRowSuggestion::new)
                        .toList(20)
                        .toObservable()
                        .map(new Function<List<Item>, PartialViewState<ViewStateSearch>>() {
                            @Override
                            public PartialViewState<ViewStateSearch> apply(List<Item> movies) throws Exception {
                                return new PartialViewStateSearch.Movies(movies);
                            }
                        })
                        .startWith(new PartialViewStateSearch.Loading(true))
                        .lift(new OnCompleteReturn<PartialViewState<ViewStateSearch>>() {
                            @Override
                            public PartialViewState<ViewStateSearch> apply(Boolean aBoolean) throws Exception {
                                return new PartialViewStateSearch.Loading(false);
                            }
                        })
                        .onErrorResumeNext(throwable -> {
                            return Observable.just(new PartialViewStateSearch.ErrorMovie(throwable));
                        })
                        .subscribeOn(io));

        Observable<PartialViewState<ViewStateSearch>> searchOpenClose = command(ViewSearch::searchOpenClose)
                .map(PartialViewStateSearch.SearchOpenClose::new);


        ViewStateSearch initial = ViewStateSearch.builder()
                .movies(Collections.<Item>emptyList())
                .loadingMovies(false)
                .errorMovies(null)
                .build();

        reduce(initial, merge(searchMovies, searchOpenClose)).subscribe(this);
    }

    @Override
    protected void render(@NonNull ViewStateSearch state, @NonNull ViewSearch view) {
        view.showLoading(state.loadingMovies());
        view.update(state.movies());
        // TODO: 2017-11-15 show error as a list item
        Throwable error = state.errorMovies();
        if (error != null) {
            if (error instanceof UnknownHostException || error instanceof SocketTimeoutException) {
                view.showError("Network Error, Are you Connected?");
                Crashlytics.log(Log.INFO, error.getClass().getSimpleName(), error.getMessage());
            } else {
                view.showError("Internal Error");
                if (BuildConfig.DEBUG) error.printStackTrace();
                Crashlytics.logException(error);
            }
        }
    }
}
