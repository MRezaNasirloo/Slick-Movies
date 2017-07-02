package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */

public interface RouterPopular {

    /**
     * @return only the first page with 10 item
     */
    Observable<MovieDomain> popular();

    /**
     * @param page the page number
     * @param size the size of each page
     * @return a list of popular movies base on given parameter
     */
    Observable<MovieDomain> popular(int page, int size);
}
