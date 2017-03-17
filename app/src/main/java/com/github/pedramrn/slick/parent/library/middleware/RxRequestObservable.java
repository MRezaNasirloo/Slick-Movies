package com.github.pedramrn.slick.parent.library.middleware;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-13
 */

public abstract class RxRequestObservable<T extends Observable<R>, R, P> extends IRequest {
    private Middleware[] middleware;
    private P data;
    private T rx;
    private RequestData requestData;

    abstract public T letItPass(P data);

    @Override
    public void refill() {

    }

    public RxRequestObservable<T, R, P> with(P data) {
        this.data = data;
        if (!(data instanceof RequestData)) {
            requestData = new RequestData().putParameter(data);
        }
        return this;
    }

    public RxRequestObservable<T, R, P> through(Middleware... middleware) {
        this.middleware = middleware;
        return this;
    }

    public void next() {
//        middleware.handle(this, requestData == null ? (RequestData) data : requestData);

        if (this != RequestStack.getInstance().pop()) throw new AssertionError();
        final T response = letItPass(data);
        if (rx != null) {
            rx.mergeWith(response);
        }
    }

    public void destination(T rx) {
        this.rx = rx;
    }
}
