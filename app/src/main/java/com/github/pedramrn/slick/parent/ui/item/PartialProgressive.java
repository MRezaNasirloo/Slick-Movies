package com.github.pedramrn.slick.parent.ui.item;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-07-19
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
        List<Item> movies = new ArrayList<>(items);
        Iterator<Item> iterator = movies.iterator();
        removeError(iterator);

        for (int i = 0, id = items.size(); i < count; i++, id++) {
            movies.add(itemRenderer.render(id, tag));
        }
        return movies;
    }

    protected Map<Integer, Item> reduce(@NonNull Map<Integer, Item> items) {
        TreeMap<Integer, Item> movies = new TreeMap<>(items);
        Iterator<Item> iterator = movies.values().iterator();
        removeError(iterator);

        for (int i = 0, id = items.size(); i < count; i++, id++) {
            movies.put(id, itemRenderer.render(id, tag));
        }

        return movies;
    }

    private void removeError(Iterator<Item> iterator) {
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (((RemovableOnError) item).removableByTag(tag)) {
                iterator.remove();
                break;
            }
        }
    }
}
