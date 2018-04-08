package com.github.pedramrn.slick.parent.ui.auth.state;

import com.github.pedramrn.slick.parent.ui.auth.ViewStateAuth;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-31
 */
public class SignedIn implements PartialViewState<ViewStateAuth> {

    private final UserApp user;

    public SignedIn(UserApp user) {
        this.user = user;
    }

    @Override
    public ViewStateAuth reduce(ViewStateAuth state) {
        return state.toBuilder()
                .user(user)
                .signedOut(false)
                .loading(false)
                .build();
    }
}
