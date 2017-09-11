package com.github.pedramrn.slick.parent.ui.item;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.home.item.ItemError;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-19
 */
public abstract class PartialProgressive {

    private final int count;
    private final String tag;
    private final ItemRenderer itemRenderer;

    public PartialProgressive(int count, String tag, ItemRenderer itemRenderer) {
        this.count = count;
        this.tag = tag;
        this.itemRenderer = itemRenderer;
    }

    protected List<Item> reduce(@NonNull List<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof ItemError) {
                items.remove(i);
                break;
            }
        }
        List<Item> progressive = new ArrayList<>(count);
        for (int i = 0, id = items.size(); i < count; i++, id++) {
            progressive.add(itemRenderer.render(id, tag));
        }
        if (!items.isEmpty()) {
            items.addAll(progressive);
        }
        else {
            items = progressive;
        }
        return items;
    }

    protected Map<Integer, Item> reduce(@NonNull Map<Integer, Item> items) {
        Iterator<Item> iterator = items.values().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item instanceof ItemError) {
                iterator.remove();
                break;
            }

        }

        Map<Integer, Item> progressive = new LinkedHashMap<>(count);
        for (int i = 0, id = items.size(); i < count; i++, id++) {
            progressive.put(id, itemRenderer.render(id, tag));
        }
        if (!items.isEmpty()) {
            items.putAll(progressive);
        }
        else {
            items = progressive;
        }
        return items;
    }
}
