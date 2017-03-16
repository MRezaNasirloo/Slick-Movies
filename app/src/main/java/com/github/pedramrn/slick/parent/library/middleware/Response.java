package com.github.pedramrn.slick.parent.library.middleware;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-15
 */

public class Response<R> {
    private final RequestStack routerStack;
    Callback<R> callback;

    public Response(RequestStack routerStack) {
        this.routerStack = routerStack;
    }

    public void subscribe(Callback<R> callback) {
        this.callback = callback;
        routerStack.processLastRequest();
    }

    public void subscribe() {
        this.callback = callback;
        routerStack.processLastRequest();
    }
}
