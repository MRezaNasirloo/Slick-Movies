package com.github.pedramrn.slick.parent.library.middleware;


/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public interface Middleware {


    void handle(IRequest request, RequestData date);
}
