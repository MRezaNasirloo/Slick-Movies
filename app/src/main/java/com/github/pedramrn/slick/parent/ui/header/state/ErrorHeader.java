package com.github.pedramrn.slick.parent.ui.header.state;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.header.StateHeader;
import com.mrezanasirloo.slick.uni.PartialViewState;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-03-06
 */
public class ErrorHeader implements PartialViewState<StateHeader> {

    public static final String VIDEOS = "VIDEOS";
    private final Throwable throwable;

    public ErrorHeader(Throwable throwable) {
        this.throwable = throwable;
    }

    @NonNull
    @Override
    public StateHeader reduce(@NonNull StateHeader state) {
        if (state.error() == null) {
            state = state.toBuilder().error(throwable).build();
        }
        return state;
    }
}
