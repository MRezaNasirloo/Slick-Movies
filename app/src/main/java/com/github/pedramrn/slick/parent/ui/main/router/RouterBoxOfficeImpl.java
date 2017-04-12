package com.github.pedramrn.slick.parent.ui.main.router;

import com.github.pedramrn.slick.parent.datasource.network.OmdbApi;
import com.github.pedramrn.slick.parent.datasource.network.TraktApi;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.MovieOmdb;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-10
 */

public class RouterBoxOfficeImpl implements RouterBoxOffice {

    private final TraktApi traktApi;
    private final OmdbApi omdbApi;

    @Inject
    public RouterBoxOfficeImpl(TraktApi traktApi, OmdbApi omdbApi) {
        this.traktApi = traktApi;
        this.omdbApi = omdbApi;
    }

    @Override
    public Observable<MovieItem> boxOffice() {
        return traktApi.get().flatMap(new Function<List<BoxOfficeItem>, ObservableSource<BoxOfficeItem>>() {
            @Override
            public ObservableSource<BoxOfficeItem> apply(@NonNull List<BoxOfficeItem> boxOfficeItems) throws Exception {
                return Observable.fromIterable(boxOfficeItems);
            }
        }).flatMap(new Function<BoxOfficeItem, ObservableSource<MovieItem>>() {
            @Override
            public ObservableSource<MovieItem> apply(@NonNull final BoxOfficeItem boxOfficeItem) throws Exception {
                return omdbApi.get(boxOfficeItem.movie.ids.imdb).map(new Function<MovieOmdb, MovieItem>() {
                    @Override
                    public MovieItem apply(@NonNull MovieOmdb movieOmdb) throws Exception {
                        return MovieItem.create(movieOmdb.title, boxOfficeItem.revenue.toString(), movieOmdb.poster, movieOmdb.metascore,
                                movieOmdb.imdbRating, movieOmdb.imdbVotes, movieOmdb.rated, movieOmdb.runtime, movieOmdb.genre, movieOmdb.director,
                                movieOmdb.writer, movieOmdb.actors, movieOmdb.plot, movieOmdb.production, movieOmdb.imdbID,
                                boxOfficeItem.movie.ids.trakt);
                    }
                });
            }
        });
    }

    public Observable<BoxOfficeItem> boxOfficepage() {
        return traktApi.get().flatMap(new Function<List<BoxOfficeItem>, ObservableSource<BoxOfficeItem>>() {
            @Override
            public ObservableSource<BoxOfficeItem> apply(@NonNull List<BoxOfficeItem> boxOfficeItems) throws Exception {
                return Observable.fromIterable(boxOfficeItems);
            }
        });
    }

    @Override
    public Observable<MovieItem> getStream(final Observable<Object> observable) {
        final PublishSubject<MovieItem> subject = PublishSubject.create();
        return traktApi.get().flatMap(new Function<List<BoxOfficeItem>, ObservableSource<Queue<BoxOfficeItem>>>() {
            @Override
            public ObservableSource<Queue<BoxOfficeItem>> apply(@NonNull List<BoxOfficeItem> boxOfficeItems) throws Exception {
                final Queue<BoxOfficeItem> q = new LinkedList<>(boxOfficeItems);
                return Observable.just(q);
            }
        }).flatMap(new Function<Queue<BoxOfficeItem>, ObservableSource<MovieItem>>() {
            @Override
            public ObservableSource<MovieItem> apply(@NonNull final Queue<BoxOfficeItem> boxOfficeItems) throws Exception {
                return observable.flatMap(new Function<Object, ObservableSource<MovieItem>>() {
                    @Override
                    public ObservableSource<MovieItem> apply(@NonNull Object o) throws Exception {
                        if (boxOfficeItems.size() > 0) {
                            final BoxOfficeItem boxOfficeItem = boxOfficeItems.poll();
                            omdbApi.get(boxOfficeItem.movie.ids.imdb).map(new Function<MovieOmdb, MovieItem>() {
                                @Override
                                public MovieItem apply(@NonNull MovieOmdb movieOmdb) throws Exception {
                                    return MovieItem.create(movieOmdb.title, boxOfficeItem.revenue.toString(), movieOmdb.poster, movieOmdb.metascore,
                                            movieOmdb.imdbRating, movieOmdb.imdbVotes, movieOmdb.rated, movieOmdb.runtime, movieOmdb.genre,
                                            movieOmdb.director,
                                            movieOmdb.writer, movieOmdb.actors, movieOmdb.plot, movieOmdb.production, movieOmdb.imdbID,
                                            boxOfficeItem.movie.ids.trakt);
                                }
                            }).subscribe(subject);
                        }
                        return subject;
                    }
                });
            }
        });
        observable.flatMap(new Function<Object, ObservableSource<MovieItem>>() {
            @Override
            public ObservableSource<MovieItem> apply(@NonNull Object o) throws Exception {
                if (boxOfficeItems.size() > 0) {
                    final BoxOfficeItem boxOfficeItem = boxOfficeItems.poll();
                    omdbApi.get(boxOfficeItem.movie.ids.imdb).map(new Function<MovieOmdb, MovieItem>() {
                        @Override
                        public MovieItem apply(@NonNull MovieOmdb movieOmdb) throws Exception {
                            return MovieItem.create(movieOmdb.title, boxOfficeItem.revenue.toString(), movieOmdb.poster, movieOmdb.metascore,
                                    movieOmdb.imdbRating, movieOmdb.imdbVotes, movieOmdb.rated, movieOmdb.runtime, movieOmdb.genre,
                                    movieOmdb.director,
                                    movieOmdb.writer, movieOmdb.actors, movieOmdb.plot, movieOmdb.production, movieOmdb.imdbID,
                                    boxOfficeItem.movie.ids.trakt);
                        }
                    }).subscribe(subject);
                }
                return subject;
            }
        });
    }

    /*@Override
    public Observable<Item> get() {
        return IdApi.get().flatMap(new Function<List<String>, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull List<String> ids) throws Exception {
                return Observable.fromIterable(ids);
            }
        }).flatMap(new Function<String, ObservableSource<Item>>() {
            @Override
            public ObservableSource<Item> apply(@NonNull final String id) throws Exception {
                return dataApi.get(id).map(new Function<Data, Item>() {
                    @Override
                    public Item apply(@NonNull Data data) throws Exception {
                        return new Item(data , id);
                });
            }
        });
    }*/
}
