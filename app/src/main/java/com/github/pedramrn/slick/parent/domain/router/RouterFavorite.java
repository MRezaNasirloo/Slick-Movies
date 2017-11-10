package com.github.pedramrn.slick.parent.domain.router;

import com.github.slick.middleware.BundleSlick;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-09
 */

public interface RouterFavorite {
    /**
     * Adds an item into commandFavorite list
     *
     * @return haven't decided
     * @param bundle
     */
    Observable<Object> add(BundleSlick bundle);

    /**
     * Removes an item into commandFavorite list
     *
     * @return haven't decided
     * @param bundle
     */

    Observable<Object> remove(BundleSlick bundle);


    /**
     * @return updates on new Objects
     * @param bundle
     */
    Observable<Boolean> updateStream(BundleSlick bundle);
}
