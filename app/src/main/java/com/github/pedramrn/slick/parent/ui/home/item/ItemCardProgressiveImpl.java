package com.github.pedramrn.slick.parent.ui.home.item;

import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public class ItemCardProgressiveImpl implements ItemCard {
    private int id;

    public ItemCardProgressiveImpl(int id) {
        this.id = id;
    }

    @Override
    public Item render(int id) {
        return new ItemCardMovieProgressive(this.id);
    }

    @Override
    public long itemId() {
        return id;
    }
}
