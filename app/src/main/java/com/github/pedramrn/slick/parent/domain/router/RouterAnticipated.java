package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktFull;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.VideoDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */

public interface RouterAnticipated {
    Observable<MovieTraktFull> anticipated();

    Observable<MovieDomain> anticipated2();
    Observable<VideoDomain> anticipated3();
}
