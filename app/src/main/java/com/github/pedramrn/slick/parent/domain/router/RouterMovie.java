package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public interface RouterMovie {
    Observable<MovieDomain> movie(Integer tmdbId);
    Observable<MovieDomain> movie(Integer... tmdbIds);

    Observable<MovieDomain> movie(List<FavoriteDomain> favorites);
}
