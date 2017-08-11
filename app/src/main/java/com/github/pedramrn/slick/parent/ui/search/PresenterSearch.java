package com.github.pedramrn.slick.parent.ui.search;

import com.github.pedramrn.slick.parent.ui.PresenterBase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

/**
 * A simple Presenter
 */
public class PresenterSearch extends PresenterBase<ViewSearch, ViewStateSearch> {

    @Inject
    public PresenterSearch(@Named("main") Scheduler main, @Named("io") Scheduler io) {
        super(main, io);
    }
}
