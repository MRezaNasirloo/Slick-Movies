package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.model.PersonDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */

public interface RouterSearch {

    /**
     * @param page the page number
     * @return a list of movies which match the given query parameter
     */
    Observable<PagedDomain<MovieSmallDomain>> movies(String query, int page);

    /**
     * @param page the page number
     * @return a list of persons who match the given query parameter
     */
    Observable<PagedDomain<PersonDomain>> persons(String query, int page);
}
