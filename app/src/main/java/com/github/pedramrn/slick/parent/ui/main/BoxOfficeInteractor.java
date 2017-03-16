package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.datasource.network.BoxOfficeWeekend;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.library.intractor.InteractorObservable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public class BoxOfficeInteractor extends InteractorObservable<List<BoxOfficeItem>, Void> {

    private final BoxOfficeWeekend boxOfficeWeekend;
    private final MiddlewareHasLoggedIn middleware;

    @Inject
    public BoxOfficeInteractor(BoxOfficeWeekend boxOfficeWeekend, MiddlewareHasLoggedIn middleware) {
        this.boxOfficeWeekend = boxOfficeWeekend;
        this.middleware = middleware;
    }

    @Override
    public Observable<List<BoxOfficeItem>> execute() {
        // TODO: 2017-03-12 map too pojo
        // TODO: 2017-03-12 middleware goes here
        // TODO: 2017-03-12 router goes here
        //someMiddleware.middleware()
        return Observable.never();
    }
}
