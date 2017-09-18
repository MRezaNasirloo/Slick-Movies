package com.github.pedramrn.slick.parent.ui.home.state;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */
@AutoValue
public abstract class ViewStateHome {
    public abstract List<Item> upcoming();

    @Nullable
    public abstract Throwable errorUpcoming();

    public abstract Builder toBuilder();

    public static ViewStateHome create(List<Item> upcoming, Throwable errorUpcoming) {
        return builder()
                .upcoming(upcoming)
                .errorUpcoming(errorUpcoming)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateHome.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder upcoming(List<Item> upcoming);

        public abstract Builder errorUpcoming(Throwable errorUpcoming);

        public abstract ViewStateHome build();
    }
}
