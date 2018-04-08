package com.github.pedramrn.slick.parent.ui.details;

import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-15
 */

public abstract class ErrorAbs implements PartialViewState<ViewStateDetails> {
    protected final Throwable throwable;

    ErrorAbs(Throwable throwable) {
        this.throwable = throwable;
    }
}
