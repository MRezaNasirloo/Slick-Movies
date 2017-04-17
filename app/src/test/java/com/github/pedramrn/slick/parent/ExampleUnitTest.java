package com.github.pedramrn.slick.parent;

import android.util.Log;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        final Flowable<Integer> observable = Flowable.just(1, 2, 3, 4, 4 , 4 ,5 ,6);

        observable.distinctUntilChanged(new BiPredicate<Integer, Integer>() {
            @Override
            public boolean test(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                System.out.println("comparing integer = [" + integer + "], integer2 = [" + integer2 + "]");
                return integer.equals(integer2);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("integer = [" + integer + "]");
            }
        });

    }

}