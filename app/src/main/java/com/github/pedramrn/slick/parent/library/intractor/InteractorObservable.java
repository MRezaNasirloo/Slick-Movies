package com.github.pedramrn.slick.parent.library.intractor;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public abstract class InteractorObservable<R, P> {

    public Observable<R> execute(P p) {
        throw new UnsupportedOperationException("Unimplemented method");
    }

    public Observable<R> execute() {
        throw new UnsupportedOperationException("Unimplemented method");
    }
}
