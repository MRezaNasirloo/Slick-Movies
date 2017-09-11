package com.github.pedramrn.slick.parent.domain.rx;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-10
 */
public class PassThroughMapTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void test() throws Exception {
        final List<Integer> list = Arrays.asList(1, -1, 2, -2, 3, -3);
        Observable.just(1, 2, 3)
                .lift(new PassThroughMap<Integer>() {
                    @Override
                    public Observable<Integer> apply(@NonNull Integer integer) throws Exception {
                        Random random = new Random();
                        int low = 0;
                        int high = 5;
                        int delay = random.nextInt(high - low) + low;
                        return Observable.just(integer * -1)
                                .delay(delay, TimeUnit.SECONDS);
                    }
                })
                .buffer(6)
                .test()
                .await()
                .assertValueCount(1)
                .assertValue(new Predicate<List<Integer>>() {
                    @Override
                    public boolean test(@NonNull List<Integer> integers) throws Exception {
                        return integers.size() == 6 && integers.containsAll(list);
                    }
                })
                .assertNoErrors()
                .assertComplete()
        ;
    }
}