package com.github.pedramrn.slick.parent.util;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-30
 */

public class ListToObserable<T1> implements Function<List<T1>, ObservableSource<T1>> {
    @Override
    public ObservableSource<T1> apply(@NonNull List<T1> t1) throws Exception {
        return Observable.fromIterable(t1);
    }
}
