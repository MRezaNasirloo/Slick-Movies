package com.github.pedramrn.slick.parent.ui.details;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */

public interface PartialViewState<T> {
    T reduce(T state);
}
