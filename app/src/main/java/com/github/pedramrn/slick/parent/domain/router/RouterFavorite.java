package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
import com.github.slick.middleware.BundleSlick;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-09
 */

public interface RouterFavorite {
    /**
     * Adds an item into commandFavorite list
     *
     * @param bundle params
     * @return haven't decided
     */
    Observable<Object> add(BundleSlick bundle);

    /**
     * Removes an item into commandFavorite list
     *
     * @param bundle params
     * @return haven't decided
     */

    Observable<Object> remove(BundleSlick bundle);


    /**
     * @param bundle params
     * @return updates on new Objects
     */
    Observable<Boolean> updateStream(BundleSlick bundle);

    /**
     * @param uid signed user id
     * @return updates on new Objects
     */
    Observable<List<FavoriteDomain>> updateStream(String uid);

}
