package com.github.pedramrn.slick.parent.ui.details;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-15
 */

public abstract class Error implements PartialViewState<ViewStateDetails> {
    protected final Throwable throwable;

    Error(Throwable throwable) {
        this.throwable = throwable;
    }
}
