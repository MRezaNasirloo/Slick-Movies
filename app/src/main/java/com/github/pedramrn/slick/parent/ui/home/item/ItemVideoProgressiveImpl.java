package com.github.pedramrn.slick.parent.ui.home.item;

import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public class ItemVideoProgressiveImpl implements ItemVideo {
    @Override
    public Item render(int id) {
        return new ItemAnticipatedProgressive(id);
    }
}
