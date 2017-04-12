package com.github.pedramrn.slick.parent;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        final Flowable<Integer> observable = Flowable.just(1, 2, 3, 4);

        observable.take(1).test().assertValue(1);
        observable.take(1).test().assertValue(2);

        getPage(0).concatWith(Observable.range(0, 10)
            .concatMap(new Function<Integer, ObservableSource<? extends String>>() {
                @Override
                public ObservableSource<? extends String> apply(@NonNull Integer integer) throws Exception {
                    return null;
                }
            })
        );
    }

    Observable<String> getPage(final int page) {
        return Observable.just("id");
    }

}