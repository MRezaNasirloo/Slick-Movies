package com.github.pedramrn.slick.parent.ui.details.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktFull;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.rx.PassThroughMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
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
        return apiTmdb.movieFull(tmdbId)
                .map(mapperMovie)
                .lift(new PassThroughMap<MovieDomain>() {
                    @Override
                    public Observable<MovieDomain> apply(@NonNull final MovieDomain movieDomain) throws Exception {
                        return apiTrakt.movie(movieDomain.imdbId()).map(new Function<MovieTraktFull, MovieDomain>() {
                            @Override
                            public MovieDomain apply(@NonNull MovieTraktFull movieTraktFull) throws Exception {
                                String certification = movieTraktFull.certification();
                                return movieDomain.toBuilder()
                                        .voteAverageTrakt(movieTraktFull.rating())
                                        .voteCountTrakt(movieTraktFull.votes())
                                        .certification(certification == null ? "n/a" : certification)
                                        .build();
                            }
                        });
                    }
                });
    }

}
