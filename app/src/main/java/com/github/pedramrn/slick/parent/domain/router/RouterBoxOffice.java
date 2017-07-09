package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-12
 */

public interface RouterBoxOffice {
    Observable<MovieDomain> boxOffice(Observable<Integer> trigger, int buffer);
}
