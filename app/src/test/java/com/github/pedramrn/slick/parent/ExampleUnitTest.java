package com.github.pedramrn.slick.parent;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        final List<Integer> integers = Arrays.asList(1, 2, 3);
        final Integer reduce = reduce(0, integers, new CallBack<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, @NonNull Integer integer2) {
                return integer + integer2;
            }
        });
        assertEquals(6, reduce.intValue());

    }

    @SuppressWarnings("TypeParameterHidesVisibleType")
    public <R> R reduce(List<R> list, CallBack<R, R, R> callBack) {
        R acc = null;
        for (R integer : list) {
            acc = callBack.apply(acc, integer);
        }

        return acc;
    }

    @SuppressWarnings("TypeParameterHidesVisibleType")
    public <R> R reduce(R seed, List<R> list, CallBack<R, R, R> callBack) {
        R acc = seed;
        for (R integer : list) {
            acc = callBack.apply(acc, integer);
        }

        return acc;
    }

    public int reduce() {
        return Observable.just(1, 2, 3).reduce(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                return integer + integer2;
            }
        }).blockingGet();
    }

    private interface CallBack<T, T1, T2> {

        public T apply(T acc, T integer);
    }
}