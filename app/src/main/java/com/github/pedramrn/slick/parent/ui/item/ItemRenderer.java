package com.github.pedramrn.slick.parent.ui.item;

import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-19
 */
public interface ItemRenderer<T extends Item & RemovableOnError> {
    T render(long id, String tag);
}
