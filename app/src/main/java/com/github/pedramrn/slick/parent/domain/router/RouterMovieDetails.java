package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-02
 */

public interface RouterMovieDetails {

    Observable<MovieDomain> get(String movieId);

    Observable<MovieDomain> get(Integer movieId);
}
