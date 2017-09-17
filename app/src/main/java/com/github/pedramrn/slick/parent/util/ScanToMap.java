package com.github.pedramrn.slick.parent.util;

import android.annotation.SuppressLint;

import com.xwray.groupie.Item;

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
public class ScanToMap<T extends Item> implements ObservableTransformer<T, Map<Integer, T>> {
    @Override
    public ObservableSource<Map<Integer, T>> apply(Observable<T> upstream) {
        return upstream.map(new Function<T, Map<Integer, T>>() {
            @Override
            public Map<Integer, T> apply(@NonNull T data) throws Exception {
                @SuppressLint("UseSparseArrays") Map<Integer, T> set = new TreeMap<>();
                set.put(((int) data.getId()), data);
                return set;
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
