package com.github.pedramrn.slick.parent.library.middleware;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-16
 */

public abstract class IRequest {
    public abstract void next();
    abstract void refill();
}
