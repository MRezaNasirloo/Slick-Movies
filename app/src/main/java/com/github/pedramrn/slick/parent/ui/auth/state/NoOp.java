package com.github.pedramrn.slick.parent.ui.auth.state;

import com.github.pedramrn.slick.parent.ui.auth.ViewStateAuth;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-31
 */
public class NoOp implements PartialViewState<ViewStateAuth> {

    public NoOp() {
    }

    @Override
    public ViewStateAuth reduce(ViewStateAuth state) {
        return state;
    }
}
