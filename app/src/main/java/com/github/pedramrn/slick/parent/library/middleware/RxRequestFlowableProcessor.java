package com.github.pedramrn.slick.parent.library.middleware;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-13
 */

public abstract class RxRequestFlowableProcessor<T extends Flowable<R>, R, P> extends IRequest {
    private Middleware[] middleware;
    private P data;
    private T rx;
    private RequestData requestData;

    abstract public T letItPass(P data);

    public RxRequestFlowableProcessor<T, R, P> with(P data) {
        this.data = data;
        if (!(data instanceof RequestData)) {
            requestData = new RequestData().putParameter(data);
        }
        return this;
    }

    public RxRequestFlowableProcessor<T, R, P> through(Middleware... middleware) {
        this.middleware = middleware;
        return this;
    }

    public void next() {
        /*for (Middleware middleware : this.middleware) {
            if (!middleware.handle(requestData == null ? (RequestData) data : requestData)) {
                return;
            }
        }*/
        if (this != RequestStack.getInstance().pop()) throw new AssertionError();
        rx.mergeWith(letItPass(data));
    }

    public void destination(T rx) {
        this.rx = rx;
    }
}
