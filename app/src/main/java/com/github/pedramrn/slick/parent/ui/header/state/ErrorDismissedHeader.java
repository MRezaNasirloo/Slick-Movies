package com.github.pedramrn.slick.parent.ui.header.state;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.header.StateHeader;
import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-03-06
 */
public class ErrorDismissedHeader implements PartialViewState<StateHeader> {

    @NonNull
    @Override
    public StateHeader reduce(@NonNull StateHeader state) {
        return state.toBuilder().error(null).build();
    }
}
