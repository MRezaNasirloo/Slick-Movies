package com.github.pedramrn.slick.parent.library.middleware;

import android.app.Activity;

import java.util.Stack;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-13
 */

public class RequestStack {

    private static RequestStack requestStack;
    private final Stack<IRequest> stack;
    private boolean changingConfigurations;

    public static RequestStack getInstance() {
        if (requestStack == null) {
            requestStack = new RequestStack();
        }
        return requestStack;
    }

    private RequestStack() {
        stack = new Stack<>();
    }


    public IRequest push(IRequest request) {
        if (stack.contains(request)) {
            return request;
        }
        stack.push(request);
        return request;
    }

    public void processLastRequest() {
        if (!changingConfigurations && stack.size() > 0) {
            final IRequest peek = stack.peek();
            peek.refill();
            peek.next();
        }
    }

    public IRequest pop() {
        return stack.pop();
    }

    public IRequest peek() {
        return stack.peek();
    }

    public int capacity() {
        return stack.capacity();
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean contains(IRequest IRequest) {
        return stack.contains(IRequest);
    }

    public void clear() {
        stack.clear();
    }

    public void handle() {
        if (stack.size() > 0) {
            stack.pop();
        }
    }

    public void onResume(Activity activity) {
        changingConfigurations = activity.isChangingConfigurations();
    }

    public void onPause(Activity activity) {
        changingConfigurations = activity.isChangingConfigurations();
    }
}
