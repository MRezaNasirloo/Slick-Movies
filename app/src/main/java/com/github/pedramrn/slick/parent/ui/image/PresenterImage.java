package com.github.pedramrn.slick.parent.ui.image;


import android.support.annotation.NonNull;

import com.mrezanasirloo.slick.uni.SlickPresenterUni;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

/**
 * A simple Presenter
 */
public class PresenterImage extends SlickPresenterUni<ViewImage, ViewStateImage> {

    @Inject
    public PresenterImage(
            @Named("main") Scheduler main,
            @Named("io") Scheduler io
    ) {
        super(main, io);
    }

    @Override
    protected void start(ViewImage view) {

    }

    @Override
    protected void render(@NonNull ViewStateImage state, @NonNull ViewImage view) {
        // TODO: 2018-04-09 use this
    }
}
