package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */

public interface RouterPopular extends PagedRouter {

    /**
     * @return only the first page with 10 item
     */
    Observable<MovieMetadata> popular();

    /**
     * @param page the page number
     * @param size the size of each page
     * @return a list of popular movies base on given parameter
     */
    Observable<MovieMetadata> popular(int page, int size);
}
