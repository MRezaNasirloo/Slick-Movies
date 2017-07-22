package com.github.pedramrn.slick.parent.ui.home.item;

import com.github.pedramrn.slick.parent.autovalue.IncludeHashEquals;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */
@AutoValue
public abstract class ItemCardProgressiveImpl implements ItemView {

    @IncludeHashEquals
    protected abstract long id();

    @Override
    public Item render(String tag) {
        return new ItemCardMovieProgressive(id());
    }

    @Override
    public long itemId() {
        return id();
    }

    public static ItemCardProgressiveImpl create(long id) {
        return new AutoValue_ItemCardProgressiveImpl(id);
    }

}
