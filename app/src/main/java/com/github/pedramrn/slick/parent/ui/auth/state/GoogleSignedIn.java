package com.github.pedramrn.slick.parent.ui.auth.state;

import com.github.pedramrn.slick.parent.ui.auth.ViewStateAuth;
import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-03
 */

public class GoogleSignedIn implements PartialViewState<ViewStateAuth> {

    private final GugleSignInResult result;

    public GoogleSignedIn(GugleSignInResult result) {
        this.result = result;
    }

    @Override
    public ViewStateAuth reduce(ViewStateAuth state) {
        return state.toBuilder()
                .googleSignInResult(result)
                .build();
    }
}
