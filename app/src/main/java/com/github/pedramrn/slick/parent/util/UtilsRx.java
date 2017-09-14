package com.github.pedramrn.slick.parent.util;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-10
 */
public class UtilsRx {
    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static void add(CompositeDisposable compositeDisposable, Disposable disposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) { compositeDisposable = new CompositeDisposable(); }
        compositeDisposable.add(disposable);
    }
}
