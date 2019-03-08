package com.github.pedramrn.slick.parent.ui.item;

import com.xwray.groupie.Item;
/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */

public interface ItemView {
    Item render(String tag);

    long itemId();
}
