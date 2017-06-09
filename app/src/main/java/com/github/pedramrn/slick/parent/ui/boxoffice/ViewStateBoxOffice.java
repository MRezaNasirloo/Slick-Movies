package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.ui.boxoffice.model.Movie;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-12
 */

@AutoValue
public abstract class ViewStateBoxOffice {
    public abstract List<Movie> movieItems();

    public static ViewStateBoxOffice create(List<Movie> movieItems) {
        return new AutoValue_ViewStateBoxOffice(movieItems);
    }
}
