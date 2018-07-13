package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public interface ViewHome extends Retryable {
    void render(ViewStateHome state);

    @NonNull
    Observable<Object> retryUpcoming();
}
