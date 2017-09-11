package com.github.pedramrn.slick.parent.ui.details;


import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

public abstract class ControllerElm<S> extends ControllerBase implements Observer<S> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ControllerElm(@Nullable Bundle args) {
        super(args);
    }

    public ControllerElm() {
    }

    protected void add(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        dispose(compositeDisposable);
        super.onDestroy();
    }
}
