package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
@AutoValue
public abstract class ViewStateCardList {
    public abstract Map<Integer, Item> movies();

    public abstract int page();

    public abstract boolean isLoading();

    public abstract int itemLoadedCount();

    @Nullable
    public abstract Throwable error();

    public abstract Builder toBuilder();

    public static Builder builder() {return new AutoValue_ViewStateCardList.Builder();}


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder movies(Map<Integer, Item> movies);

        public abstract Builder page(int page);

        public abstract Builder isLoading(boolean isLoading);

        public abstract Builder itemLoadedCount(int itemLoadedCount);

        public abstract Builder error(Throwable error);

        public abstract ViewStateCardList build();
    }
}
