package com.github.pedramrn.slick.parent;

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
        final Flowable<Integer> observable = Flowable.just(1, 2, 3, 4);

        final PublishSubject<Integer> subject = PublishSubject.create();


        final Scheduler executor = Schedulers.from(new ThreadPoolExecutor(2, 2, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(2)));
        subject.subscribeOn(Schedulers.single())
                .observeOn(executor)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(Thread.currentThread().getName() + " integer = [" + integer + "]");
                    }
                });

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();
        subject.onNext(1);
        Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {
                subject.onNext(2);
                return Observable.just(1);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread()).subscribe();


    }

}