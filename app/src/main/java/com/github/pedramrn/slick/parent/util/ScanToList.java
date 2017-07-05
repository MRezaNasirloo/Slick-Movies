package com.github.pedramrn.slick.parent.util;

import java.util.ArrayList;
import java.util.List;

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
public class ScanToList<T> implements ObservableTransformer<T, List<T>> {
    @Override
    public ObservableSource<List<T>> apply(Observable<T> upstream) {
        return upstream.map(new Function<T, List<T>>() {
            @Override
            public List<T> apply(@NonNull T data) throws Exception {
                ArrayList<T> list = new ArrayList<>(1);
                list.add(data);
                return list;
            }
        }).scan(new BiFunction<List<T>, List<T>, List<T>>() {
            @Override
            public List<T> apply(@NonNull List<T> list, @NonNull List<T> list2) throws Exception {
                list.addAll(list2);
                return list;
            }
        });
    }
}
