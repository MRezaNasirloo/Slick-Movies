package com.github.pedramrn.slick.parent.ui.details.router;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.rx.PassThroughMap;

import javax.inject.Inject;

import io.reactivex.Observable;

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
    public Observable<MovieDomain> get(@android.support.annotation.NonNull final Integer tmdbId) {
        return apiTmdb.movieFull(tmdbId)
                .map(mapperMovie)
                .lift(new PassThroughMap<MovieDomain>() {
                    @Override
                    public Observable<MovieDomain> apply(@NonNull final MovieDomain movieDomain) throws Exception {
                        //noinspection ConstantConditions
                        if (movieDomain.imdbId() == null || movieDomain.imdbId().isEmpty()) {
                            return Observable.error(new InformationNotAvailableException());
                        }
                        return apiTrakt.movie(movieDomain.imdbId()).map(movieTraktFull -> {
                            String certification = movieTraktFull.certification();
                            return movieDomain.toBuilder()
                                    .voteAverageTrakt(movieTraktFull.rating())
                                    .voteCountTrakt(movieTraktFull.votes())
                                    .certification(certification == null ? "n/a" : certification)
                                    .build();
                        });
                    }
                });
    }

}
