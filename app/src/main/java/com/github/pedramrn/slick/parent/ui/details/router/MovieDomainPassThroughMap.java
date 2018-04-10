package com.github.pedramrn.slick.parent.ui.details.router;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.rx.PassThroughMap;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-09
 */
public class MovieDomainPassThroughMap extends PassThroughMap<MovieDomain> {

    private final ApiTrakt apiTrakt;

    @Inject
    public MovieDomainPassThroughMap(ApiTrakt apiTrakt) {
        this.apiTrakt = apiTrakt;
    }

    @Override
    public Observable<MovieDomain> apply(@NonNull final MovieDomain movieDomain) {
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
}
