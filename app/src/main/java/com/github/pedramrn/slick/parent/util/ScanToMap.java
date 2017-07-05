package com.github.pedramrn.slick.parent.util;

import android.annotation.SuppressLint;

import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;

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
public class ScanToMap implements ObservableTransformer<ItemCard, Map<Integer, ItemCard>> {
    @Override
    public ObservableSource<Map<Integer, ItemCard>> apply(Observable<ItemCard> upstream) {
        return upstream.map(new Function<ItemCard, Map<Integer, ItemCard>>() {
            @Override
            public Map<Integer, ItemCard> apply(@NonNull ItemCard data) throws Exception {
                @SuppressLint("UseSparseArrays") Map<Integer, ItemCard> set = new LinkedHashMap<>(1);
                set.put(Long.valueOf(data.itemId()).intValue(), data);
                return set;
            }
        }).scan(new BiFunction<Map<Integer, ItemCard>, Map<Integer, ItemCard>, Map<Integer, ItemCard>>() {
            @Override
            public Map<Integer, ItemCard> apply(@NonNull Map<Integer, ItemCard> set, @NonNull Map<Integer, ItemCard> set2) throws Exception {
                set.putAll(set2);
                return set;
            }
        });
    }
}
