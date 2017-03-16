package com.github.pedramrn.slick.parent.library.intractor;

import com.github.pedramrn.slick.parent.library.middleware.Middleware;
import com.github.pedramrn.slick.parent.library.ExceptionMiddlewareNotSatisfied;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public abstract class InteractorCompletable<P> {
    private static final String TAG = InteractorCompletable.class.getSimpleName();
    protected List<Middleware> middlewares = new ArrayList<>();

    protected void registerMiddleware(Middleware middleware) {
        middlewares.add(middleware);
    }

    public Completable execute(P p)throws ExceptionMiddlewareNotSatisfied {
        return Completable.never();
    }

    public Completable execute() {
        return Completable.never();
    }
}
