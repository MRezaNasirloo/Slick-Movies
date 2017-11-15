package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.ui.boxoffice.state.ViewStateBoxOffice;
import com.github.slick.SlickPresenter;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */
public class PresenterMain extends SlickPresenter<ViewMain> {
    private static final String TAG = PresenterMain.class.getSimpleName();

    private BehaviorSubject<ViewStateBoxOffice> state = BehaviorSubject.create();


    @Inject
    public PresenterMain() {
    }

    public BehaviorSubject<ViewStateBoxOffice> updateStream() {
        return state;
    }

}
