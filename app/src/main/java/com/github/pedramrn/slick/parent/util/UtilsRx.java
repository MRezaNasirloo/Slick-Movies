package com.github.pedramrn.slick.parent.util;

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
}
