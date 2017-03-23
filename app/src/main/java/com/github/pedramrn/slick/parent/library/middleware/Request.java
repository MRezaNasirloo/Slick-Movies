package com.github.pedramrn.slick.parent.library.middleware;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-13
 */

public abstract class Request<R, P> extends IRequest {
    RequestStack routerStack = RequestStack.getInstance();
    private Middleware[] middleware;
    private P data;
    private Callback<R> callback;
    private RequestData requestData;
    private Stack<Middleware> middlewareStack = new Stack<>();
    private int middlewareBackStack = 0;
    private boolean tooLateAlreadyFinished = false;

    abstract public R destination(P data);

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
    public void refill() {
        middlewareStack.clear();
        middlewareStack.addAll(Arrays.asList(middleware));
    }

    @Override
    public void next() {
        //        for (Middleware middleware : this.middleware) {
        //            if (!middleware.handle(requestData == null ? (RequestData) data : requestData)) {
        //                return;
        //            }
        //        }
        if (middlewareStack.size() > 0) {
            middlewareStack.pop().handle(this, requestData == null ? (RequestData) data : requestData);
        }
            middlewareBackStack++;
//        if (middleware != null) {
//        }
        if (!(middlewareBackStack -1 == this.middleware.length && !tooLateAlreadyFinished)) {
            return;
        }

        if (this != routerStack.pop()) throw new AssertionError();
        final R response = destination(data);
        if (callback != null) {
            callback.onPass(response);
        }
        tooLateAlreadyFinished = true;
    }

    public void destinationCallback(Callback<R> callback) {
        this.callback = callback;
    }
}
