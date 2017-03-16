package com.github.pedramrn.slick.parent.library.intractor;

import io.reactivex.Single;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public abstract class InteractorSingle<R, P> {

    public abstract Single<R> execute(P p);

    public abstract Single<R> execute();
}
