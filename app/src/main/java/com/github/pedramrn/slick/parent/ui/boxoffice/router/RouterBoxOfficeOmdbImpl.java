package com.github.pedramrn.slick.parent.ui.boxoffice.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.MovieOmdb;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-10
 */

public class RouterBoxOfficeOmdbImpl implements RouterBoxOffice {

    private final ApiTrakt apiTrakt;
    private final ApiOmdb apiOmdb;
    private final Scheduler io;
    private static final String TAG = RouterBoxOfficeOmdbImpl.class.getSimpleName();

    @Inject
    public RouterBoxOfficeOmdbImpl(ApiTrakt apiTrakt, ApiOmdb apiOmdb, @Named("io") Scheduler io) {
        this.apiTrakt = apiTrakt;
        this.apiOmdb = apiOmdb;
        this.io = io;
    }

    @Override
    public Observable<MovieItem> boxOffice(Observable<Integer> trigger, int buffer) {
        return apiTrakt.get()
                .flatMap(new Function<List<BoxOfficeItem>, ObservableSource<BoxOfficeItem>>() {
                    @Override
                    public ObservableSource<BoxOfficeItem> apply(@NonNull List<BoxOfficeItem> boxOfficeItems) throws Exception {
                        return Observable.fromIterable(boxOfficeItems);
                    }
                })
                .buffer(buffer)
                .zipWith(trigger, new BiFunction<List<BoxOfficeItem>, Integer, List<BoxOfficeItem>>() {
                    @Override
                    public List<BoxOfficeItem> apply(@NonNull List<BoxOfficeItem> boxOfficeItem, @NonNull Integer ignored) throws Exception {
                        return boxOfficeItem;
                    }
                })
                .concatMap(new Function<List<BoxOfficeItem>, ObservableSource<MovieItem>>() {
                    @Override
                    public ObservableSource<MovieItem> apply(@NonNull final List<BoxOfficeItem> boxOfficeItem) throws Exception {
                        return Observable.fromIterable(boxOfficeItem)
                                .concatMap(new Function<BoxOfficeItem, ObservableSource<MovieItem>>() {
                                    @Override
                                    public ObservableSource<MovieItem> apply(@NonNull final BoxOfficeItem boxOfficeItem) throws Exception {
                                        return apiOmdb.get(boxOfficeItem.movie().ids().imdb)
                                                .subscribeOn(io)
                                                .map(new Function<MovieOmdb, MovieItem>() {
                                                    @Override
                                                    public MovieItem apply(@NonNull MovieOmdb movieOmdb) throws Exception {
                                                        return MovieItem.create(movieOmdb.title(), boxOfficeItem.revenue().toString(),
                                                                movieOmdb.poster(),
                                                                movieOmdb.metascore(),
                                                                movieOmdb.imdbRating(), movieOmdb.imdbVotes(), movieOmdb.rated(), movieOmdb.runtime(),
                                                                Collections.singletonList(movieOmdb.genre()),
                                                                movieOmdb.director(),
                                                                movieOmdb.writer(), movieOmdb.actors(), movieOmdb.plot(),
                                                                Collections.singletonList(movieOmdb.production()),
                                                                movieOmdb.released(), movieOmdb.imdbID(),
                                                                boxOfficeItem.movie().ids().trakt, boxOfficeItem.movie().ids().tmdb);
                                                    }
                                                });
                                    }
                                });

                    }
                });
    }
}
