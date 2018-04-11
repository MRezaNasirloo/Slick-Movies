package com.github.pedramrn.slick.parent.utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-11
 */
public class LogIt<T> implements Observer<T> {
    private final String TAG;

    public LogIt(String TAG) {
        this.TAG = TAG;
    }

    public LogIt() {
        TAG = "LOG_IT";
    }


    @Override
    public void onSubscribe(Disposable d) {
        System.out.println(TAG + ".onSubscribe()");
    }

    @Override
    public void onNext(T onNext) {
        System.out.println(TAG + ".onNext() = [" + onNext + "]");
    }

    @Override
    public void onError(Throwable e) {
        System.out.println(TAG + ".onError() =  [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println(TAG + ".onComplete()");
    }
}
