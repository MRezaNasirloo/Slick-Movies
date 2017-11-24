package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-12
 */

public interface RouterBoxOffice {
    Observable<MovieDomain> boxOfficePagination(Observable<Integer> trigger, int buffer);

    Observable<MovieMetadata> boxOffice();
}
