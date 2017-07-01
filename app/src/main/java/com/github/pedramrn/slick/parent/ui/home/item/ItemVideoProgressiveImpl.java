package com.github.pedramrn.slick.parent.ui.home.item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public class ItemVideoProgressiveImpl implements ItemVideo {
    @Override
    public ItemAnticipated render(int id) {
        return new ItemAnticipatedProgressive(id);
    }
}
