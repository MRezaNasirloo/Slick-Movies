package com.github.pedramrn.slick.parent.library.middleware;

import com.github.pedramrn.slick.parent.ui.App;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-13
 */

public abstract class Request<R, P> implements IRequest {
    RequestStack routerStack = RequestStack.getInstance();
    private Middleware[] middleware;
    private P data;
    private Callback<R> callback;
    private RequestData requestData;

    abstract public R letItPass(P data);

    public Request<R, P> with(P data) {
        this.data = data;
        if (!(data instanceof RequestData)) {
            requestData = new RequestData().putParameter(data);
        }
        return this;
    }

    public Request<R, P> through(Middleware... middleware) {
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
        if (this != routerStack.pop()) throw new AssertionError();
            final R response = letItPass(data);
        if (callback != null) {
            callback.onPass(response);
        }
    }

    public void destination(Callback<R> callback) {
        this.callback = callback;
    }
}
