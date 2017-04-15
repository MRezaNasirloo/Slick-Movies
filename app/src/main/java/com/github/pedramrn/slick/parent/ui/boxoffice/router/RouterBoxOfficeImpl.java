package com.github.pedramrn.slick.parent.ui.boxoffice.router;

import com.github.pedramrn.slick.parent.datasource.network.OmdbApi;
import com.github.pedramrn.slick.parent.datasource.network.TraktApi;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.MovieOmdb;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-10
 */

public class RouterBoxOfficeImpl implements RouterBoxOffice {

    private final TraktApi traktApi;
    private final OmdbApi omdbApi;
    private static final String TAG = RouterBoxOfficeImpl.class.getSimpleName();

    @Inject
    public RouterBoxOfficeImpl(TraktApi traktApi, OmdbApi omdbApi) {
        this.traktApi = traktApi;
        this.omdbApi = omdbApi;
    }

    @Override
    public Observable<MovieItem> boxOffice(Observable<Integer> trigger, int buffer) {
        return traktApi.get()
                .subscribeOn(Schedulers.io())
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
                .subscribeOn(AndroidSchedulers.mainThread())
                .concatMap(new Function<List<BoxOfficeItem>, ObservableSource<MovieItem>>() {
                    @Override
                    public ObservableSource<MovieItem> apply(@NonNull final List<BoxOfficeItem> boxOfficeItem) throws Exception {
                        return Observable.fromIterable(boxOfficeItem).concatMap(new Function<BoxOfficeItem, ObservableSource<MovieItem>>() {
                            @Override
                            public ObservableSource<MovieItem> apply(@NonNull final BoxOfficeItem boxOfficeItem) throws Exception {
                                return omdbApi.get(boxOfficeItem.movie.ids.imdb)
                                        .subscribeOn(Schedulers.io())
                                        .map(new Function<MovieOmdb, MovieItem>() {
                                            @Override
                                            public MovieItem apply(@NonNull MovieOmdb movieOmdb) throws Exception {
                                                return MovieItem.create(movieOmdb.title, boxOfficeItem.revenue.toString(), movieOmdb.poster,
                                                        movieOmdb.metascore,
                                                        movieOmdb.imdbRating, movieOmdb.imdbVotes, movieOmdb.rated, movieOmdb.runtime,
                                                        movieOmdb.genre,
                                                        movieOmdb.director,
                                                        movieOmdb.writer, movieOmdb.actors, movieOmdb.plot, movieOmdb.production, movieOmdb.imdbID,
                                                        boxOfficeItem.movie.ids.trakt);
                                            }
                                        });
                            }
                        });

                    }
                });
    }
}
