package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieItem;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-12
 */

public interface RouterBoxOffice {
    Observable<MovieItem> boxOffice();

    Observable<MovieItem> getStream(Observable<Object> observable);
}
