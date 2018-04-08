package com.github.pedramrn.slick.parent.ui.auth.state;

import com.github.pedramrn.slick.parent.ui.auth.ViewStateAuth;
import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-31
 */
public class SigningError implements PartialViewState<ViewStateAuth> {

    private final Throwable throwable;

    public SigningError(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ViewStateAuth reduce(ViewStateAuth state) {
        return state.toBuilder()
                .error(throwable)
                .loading(false)
                .build();
    }
}
