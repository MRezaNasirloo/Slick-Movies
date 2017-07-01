package com.github.pedramrn.slick.parent.ui.home.item;

import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public class ItemCardProgressiveImpl implements ItemCard {
    private int id;

    @Override
    public Item render(int id) {
        this.id = id;
        return new ItemCardMovieProgressive(id);
    }

    @Override
    public long itemId() {
        return id;
    }
}
