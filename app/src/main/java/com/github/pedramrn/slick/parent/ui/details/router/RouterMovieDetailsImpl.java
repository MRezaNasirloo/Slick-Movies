package com.github.pedramrn.slick.parent.ui.details.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktFull;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public class RouterMovieDetailsImpl implements RouterMovieDetails {

    private final ApiTmdb apiTmdb;
    private final ApiTrakt apiTrakt;
    private final MapperMovie mapperMovie;

    @Inject
    public RouterMovieDetailsImpl(ApiTmdb apiTmdb, ApiTrakt apiTrakt, MapperMovie mapperMovie) {
        this.apiTmdb = apiTmdb;
        this.apiTrakt = apiTrakt;
        this.mapperMovie = mapperMovie;
    }

    @Override
    public Observable<MovieDomain> get(final Integer tmdbId) {
        return apiTmdb.movieFull(tmdbId).map(mapperMovie).concatMap(new Function<MovieDomain, ObservableSource<MovieDomain>>() {
            @Override
            public ObservableSource<MovieDomain> apply(@NonNull MovieDomain movieDomain) throws Exception {
                return Observable.just(movieDomain).scan(movieDomain, new BiFunction<MovieDomain, MovieDomain, MovieDomain>() {
                            @Override
                            public MovieDomain apply(@NonNull final MovieDomain movieDomain, @NonNull MovieDomain movieDomain2) throws Exception {
                                return apiTrakt.movie(movieDomain.imdbId()).concatMap(new Function<MovieTraktFull, Observable<MovieDomain>>() {
                                    @Override
                                    public Observable<MovieDomain> apply(@NonNull MovieTraktFull movieTraktFull) throws Exception {
                                        String certification = movieTraktFull.certification();
                                        return Observable.just(movieDomain.toBuilder()
                                                .voteAverageTrakt(movieTraktFull.rating())
                                                .voteCountTrakt(movieTraktFull.votes())
                                                .certification(certification == null ? "n/a" : certification)
                                                .build())
                                                ;
                                    }// FIXME: 2017-07-29 don't interrupt me or I crash your app
                                    // Attempt to fix by changing to concatMap from map
                                }).blockingFirst();
                            }
                        }
                );
            }
        });
    }

}
