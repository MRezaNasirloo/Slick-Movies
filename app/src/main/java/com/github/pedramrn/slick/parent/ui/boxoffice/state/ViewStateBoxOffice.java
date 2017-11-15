package com.github.pedramrn.slick.parent.ui.boxoffice.state;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-12
 */

@AutoValue
public abstract class ViewStateBoxOffice {
    public abstract List<Movie> movieItems();

    @Nullable
    public abstract Throwable error();

    public static Builder builder() {
        return new AutoValue_ViewStateBoxOffice.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder movieItems(List<Movie> movieItems);

        public abstract Builder error(Throwable error);

        public abstract ViewStateBoxOffice build();
    }
}
