package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieDetails;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-02
 */

public interface RouterMovieDetails {

   Observable<MovieDetails> get(Integer tmdbId);
}
