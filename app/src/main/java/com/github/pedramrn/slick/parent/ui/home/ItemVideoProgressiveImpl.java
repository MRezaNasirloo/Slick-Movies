package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.ui.home.item.ItemAnticipated;
import com.github.pedramrn.slick.parent.ui.home.item.ItemAnticipatedProgressive;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

class ItemVideoProgressiveImpl implements ItemVideo {
    @Override
    public ItemAnticipated render(int id) {
        return new ItemAnticipatedProgressive(id);
    }
}
