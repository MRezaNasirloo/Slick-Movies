package com.github.pedramrn.slick.parent.util;

import com.xwray.groupie.Item;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */
public class ScanListToMap<T extends Item> implements ObservableTransformer<List<T>, Map<Integer, T>> {
    @Override
    public ObservableSource<Map<Integer, T>> apply(@NonNull Observable<List<T>> upstream) {
        return upstream.map(new Function<List<T>, Map<Integer, T>>() {
            @Override
            public Map<Integer, T> apply(@NonNull List<T> ts) throws Exception {
                TreeMap<Integer, T> map = new TreeMap<>();
                Iterator<T> iterator = ts.iterator();
                while (iterator.hasNext()) {
                    T next = iterator.next();
                    map.put((int) next.getId(), next);
                }
                return map;
            }
        }).scan(new BiFunction<Map<Integer, T>, Map<Integer, T>, Map<Integer, T>>() {
            @Override
            public Map<Integer, T> apply(@NonNull Map<Integer, T> set, @NonNull Map<Integer, T> set2) throws Exception {
                set.putAll(set2);
                return set;
            }
        });
    }
}
