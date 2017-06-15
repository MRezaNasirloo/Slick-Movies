package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.ui.details.model.Movie;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

class ViewStateDetails {
    private final Movie movie;

    public ViewStateDetails(Movie movie) {
        this.movie = movie;
    }

    public Movie movieDetails() {
        return movie;
    }
}
