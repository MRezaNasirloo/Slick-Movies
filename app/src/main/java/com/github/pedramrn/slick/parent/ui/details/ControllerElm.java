package com.github.pedramrn.slick.parent.ui.details;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.util.UtilsRx;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

public abstract class ControllerElm<S> extends ControllerBase implements Observer<S> {
    public static final String TAG = ControllerElm.class.getSimpleName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ControllerElm(@Nullable Bundle args) {
        super(args);
    }

    public ControllerElm() {
    }

    protected void add(Disposable disposable) {
        if (compositeDisposable.isDisposed()) { compositeDisposable = new CompositeDisposable(); }
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        Log.d(TAG, "onDestroyView: disposing");
//        dispose(compositeDisposable);
        super.onDestroyView(view);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: disposing");
        UtilsRx.dispose(compositeDisposable);
        super.onDestroy();
    }
}
