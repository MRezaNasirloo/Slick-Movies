package com.github.pedramrn.slick.parent;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {


    final Observable<Long> interval1 = Observable.intervalRange(0, 5, 0, 1000, TimeUnit.MILLISECONDS);
    final Observable<Long> interval2 = Observable.intervalRange(20, 5, 2000, 2000, TimeUnit.MILLISECONDS);

    Observable<Long> observable = interval2.mergeWith(new Observable<Long>() {
        @Override
        protected void subscribeActual(Observer<? super Long> observer) {
            interval1.subscribe(observer);
        }
    });
    observable.subscribe(new Consumer<Long>() {
        @Override
        public void accept(@NonNull Long aLong) throws Exception {
            System.out.println("observable1 aLong = [" + aLong + "]");
        }
    });

    observable.subscribe(new Consumer<Long>() {
        @Override
        public void accept(@NonNull Long aLong) throws Exception {
            System.out.println("observable2 aLong = [" + aLong + "]");
        }
    });

    /* out put
    observable1 aLong = [0]
    observable2 aLong = [0]
    observable1 aLong = [1]
    observable2 aLong = [1]
    observable1 aLong = [2]
    observable1 aLong = [20]
    observable1 aLong = [3]
    observable1 aLong = [21]
    observable1 aLong = [4]
    observable1 aLong = [22]
    observable1 aLong = [23]
    observable1 aLong = [24]
    ---------
    expected
    observable1 aLong = [0]
    observable2 aLong = [0]
    observable1 aLong = [1]
    observable2 aLong = [1]
    observable1 aLong = [2]
    observable1 aLong = [2]
    observable1 aLong = [20]
    observable1 aLong = [3]
    observable2 aLong = [3]
    observable1 aLong = [21]
    observable1 aLong = [4]
    observable2 aLong = [4]
    observable1 aLong = [22]
    observable1 aLong = [23]
    observable1 aLong = [24]
    */
        while (true) {

        }
    }

}
