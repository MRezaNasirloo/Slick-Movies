package com.github.pedramrn.slick.parent.ui.auth.state;

import com.github.pedramrn.slick.parent.ui.auth.ViewStateAuth;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-31
 */
public class SignInDialog implements PartialViewState<ViewStateAuth> {

    public SignInDialog() {
    }

    @Override
    public ViewStateAuth reduce(ViewStateAuth state) {
        return state.toBuilder()
                .user(null)
                .showSignInDialog(true)
                .signedOut(null)
                .build();
    }
}
