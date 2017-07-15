package com.github.pedramrn.slick.parent.util;

import android.annotation.SuppressLint;

import com.github.pedramrn.slick.parent.ui.home.item.ItemView;

import java.util.LinkedHashMap;
import java.util.Map;

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
public class ScanToMap implements ObservableTransformer<ItemView, Map<Integer, ItemView>> {
    @Override
    public ObservableSource<Map<Integer, ItemView>> apply(Observable<ItemView> upstream) {
        return upstream.map(new Function<ItemView, Map<Integer, ItemView>>() {
            @Override
            public Map<Integer, ItemView> apply(@NonNull ItemView data) throws Exception {
                @SuppressLint("UseSparseArrays") Map<Integer, ItemView> set = new LinkedHashMap<>(1);
                set.put(Long.valueOf(data.itemId()).intValue(), data);
                return set;
            }
        }).scan(new BiFunction<Map<Integer, ItemView>, Map<Integer, ItemView>, Map<Integer, ItemView>>() {
            @Override
            public Map<Integer, ItemView> apply(@NonNull Map<Integer, ItemView> set, @NonNull Map<Integer, ItemView> set2) throws Exception {
                set.putAll(set2);
                return set;
            }
        });
    }
}
