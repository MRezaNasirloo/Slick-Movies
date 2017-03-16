package com.github.pedramrn.slick.parent.library.middleware;

import io.reactivex.subjects.CompletableSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-13
 */

public abstract class RxRequestCompletable<T extends CompletableSubject, P> implements IRequest {
    private Middleware[] middleware;
    private P data;
    private T rx;
    private RequestData requestData;

    abstract public T letItPass(P data);

    public RxRequestCompletable<T, P> with(P data) {
        this.data = data;
        if (!(data instanceof RequestData)) {
            requestData = new RequestData().putParameter(data);
        }
        return this;
    }

    public RxRequestCompletable<T, P> through(Middleware... middleware) {
        this.middleware = middleware;
        return this;
    }

    @Override
    public void next() {
        for (Middleware middleware : this.middleware) {
            if (!middleware.handle(requestData == null ? (RequestData) data : requestData)) {
                return;
            }
        }
        if (this != RequestStack.getInstance().pop()) throw new AssertionError();
        final T response = letItPass(data);
        if (rx != null) {
            rx.mergeWith(response);
        }
    }

    public void destination(T rx) {
        this.rx = rx;
    }

    /*public void destination(Callback<R> callback) {
        this.callback = callback;
    }

    public void destination(Callback<R> callback) {
        this.callback = callback;
    }*/
}
