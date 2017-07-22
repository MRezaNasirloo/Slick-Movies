package com.github.pedramrn.slick.parent.ui.item;

import com.github.pedramrn.slick.parent.util.IdBank;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-19
 */
public abstract class PartialProgressive {

    protected final List<Item> progressive;

    public PartialProgressive(int count, String tag, ItemRenderer itemRenderer) {
        progressive = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int id = IdBank.nextId(tag);
            progressive.add(itemRenderer.render(id, tag));
        }
    }

    public PartialProgressive(String tag, ItemRenderer itemRenderer) {
        progressive = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            int id = IdBank.nextId(tag);
            progressive.add(itemRenderer.render(id, tag));
        }
    }

    protected List<Item> reduce(List<Item> items) {
        if (items != null && items.size() > 0) {
            items.addAll(progressive);
        } else {
            items = progressive;
        }
        return items;
    }
}
