package com.github.pedramrn.slick.parent.ui.boxoffice.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.domain.mapper.MapperSimpleData;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;

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

public class RouterBoxOfficeTmdbImpl implements RouterBoxOffice {

    private final ApiTrakt apiTrakt;
    private final ApiTmdb apiTmdb;
    private final MapperSimpleData mapper;
    private final Scheduler io;
    private static final String TAG = RouterBoxOfficeTmdbImpl.class.getSimpleName();

    @Inject
    public RouterBoxOfficeTmdbImpl(ApiTrakt apiTrakt, ApiTmdb apiTmdb, MapperSimpleData mapper, @Named("io") Scheduler io) {
        this.apiTrakt = apiTrakt;
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
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
                                        return apiTmdb.movie(boxOfficeItem.movie().ids().tmdb())
                                                .subscribeOn(io)
                                                .map(new Function<MovieTmdb, MovieItem>() {
                                                    @Override
                                                    public MovieItem apply(@NonNull MovieTmdb movieTmdb) throws Exception {
                                                        return MovieItem.create(movieTmdb.title(), boxOfficeItem.revenue().toString(),
                                                                movieTmdb.posterPath(),
                                                                "", "", "", "",
                                                                movieTmdb.runtime().toString(),
                                                                Observable.fromIterable(movieTmdb.genres())
                                                                        .map(mapper)
                                                                        .toList()
                                                                        .blockingGet(),
                                                                "", "", "",
                                                                movieTmdb.overview(),
                                                                Observable.fromIterable(movieTmdb.productionCompanies())
                                                                        .map(mapper)
                                                                        .toList()
                                                                        .blockingGet(),
                                                                movieTmdb.releaseDate(), movieTmdb.imdbId(),
                                                                boxOfficeItem.movie().ids().trakt(), boxOfficeItem.movie().ids().tmdb());
                                                    }
                                                });
                                    }
                                });

                    }
                });
    }
}
