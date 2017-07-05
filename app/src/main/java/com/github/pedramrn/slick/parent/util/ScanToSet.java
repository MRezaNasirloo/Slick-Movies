package com.github.pedramrn.slick.parent.util;

import java.util.LinkedHashSet;
import java.util.Set;

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
public class ScanToSet<T> implements ObservableTransformer<T, Set<T>> {
    @Override
    public ObservableSource<Set<T>> apply(Observable<T> upstream) {
        return upstream.map(new Function<T, Set<T>>() {
            @Override
            public Set<T> apply(@NonNull T data) throws Exception {
                Set<T> set = new LinkedHashSet<T>(1);
                set.add(data);
                return set;
            }
        }).scan(new BiFunction<Set<T>, Set<T>, Set<T>>() {
            @Override
            public Set<T> apply(@NonNull Set<T> set, @NonNull Set<T> set2) throws Exception {
                set.addAll(set2);
                return set;
            }
        });
    }
}
