package com.github.pedramrn.slick.parent.ui.image;

import com.github.pedramrn.slick.parent.ui.PresenterBase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

/**
 * A simple Presenter
 */
public class PresenterImage extends PresenterBase<ViewImage, ViewStateImage> {

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
}
