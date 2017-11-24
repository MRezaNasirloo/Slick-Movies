package com.github.pedramrn.slick.parent.ui.boxoffice.state;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-12
 */

@AutoValue
public abstract class ViewStateBoxOffice {
    public abstract Map<Integer, Item> movies();

    @Nullable
    public abstract Throwable error();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ViewStateBoxOffice.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder error(Throwable error);

        public abstract Builder movies(Map<Integer, Item> movieItems);

        public abstract ViewStateBoxOffice build();
    }
}
