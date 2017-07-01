package com.github.pedramrn.slick.parent.ui.home.item;

import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */

public interface ItemCard {
    Item render(int id);

    long itemId();
}
