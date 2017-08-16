package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public interface ViewDetails {
    void render(ViewStateDetails viewStateDetails);

    MovieBasic getMovie();
}
