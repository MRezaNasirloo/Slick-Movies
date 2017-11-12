package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;

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
     *
     * @param favorite@return haven't decided
     */
    Observable<Object> add(FavoriteDomain favorite);

    /**
     * Removes an item into commandFavorite list
     *
     *
     * @param favorite@return haven't decided
     */

    Observable<Object> remove(FavoriteDomain favorite);


    /**
     *
     * @param tmdbId@return updates on new Objects
     */
    Observable<Boolean> updateStream(Integer tmdbId);

    /**
     * @return updates on new Objects
     */
    Observable<List<FavoriteDomain>> updateStream();

}
