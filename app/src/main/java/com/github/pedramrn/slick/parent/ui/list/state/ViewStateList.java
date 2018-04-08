package com.github.pedramrn.slick.parent.ui.list.state;

import com.google.auto.value.AutoValue;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-05
 */

@AutoValue
public abstract class ViewStateList {

    public abstract List<Item> items();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ViewStateList.Builder();
    }


    public static class ItemList implements PartialViewState<ViewStateList> {

        private final List<Item> items;

        public ItemList(List<Item> items) {
            this.items = items;
        }

        @Override
        public ViewStateList reduce(ViewStateList state) {
            return state.toBuilder().items(items).build();
        }
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder items(List<Item> items);

        public abstract ViewStateList build();
    }
}
