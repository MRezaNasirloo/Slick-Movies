package com.github.pedramrn.slick.parent.library.intractor;

import io.reactivex.Flowable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public abstract class InteractorFlowable<R, P> {

    public abstract Flowable<R> execute(P p);

    public abstract Flowable<R> execute();
}
