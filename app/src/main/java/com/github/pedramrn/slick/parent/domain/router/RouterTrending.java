package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */

public interface RouterTrending {
    /**
     * @return only the first page with 10 item
     */
    Observable<MovieMetadata> trending();

    /**
     * @param page the page number
     * @param size the size of each page
     * @return a list of trending movies base on given parameter
     */
    Observable<MovieMetadata> trending(int page, int size);

    Observable<MovieDomain> trending(Integer... ids);
}
