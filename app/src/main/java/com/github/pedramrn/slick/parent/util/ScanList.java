package com.github.pedramrn.slick.parent.util;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */
public class ScanList<T> implements ObservableTransformer<List<T>, List<T>> {
    @Override
    public ObservableSource<List<T>> apply(Observable<List<T>> upstream) {
        return upstream.scan(new BiFunction<List<T>, List<T>, List<T>>() {
            @Override
            public List<T> apply(@NonNull List<T> list, @NonNull List<T> list2) throws Exception {
                list.addAll(list2);
                return list;
            }
        });
    }
}
