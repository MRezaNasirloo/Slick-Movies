package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-12
 */

@AutoValue
public abstract class ViewStateBoxOffice {
    public abstract List<MovieBoxOffice> movieItems();

    public static ViewStateBoxOffice create(List<MovieBoxOffice> movieBoxOfficeItems) {
        return new AutoValue_ViewStateBoxOffice(movieBoxOfficeItems);
    }
}
