package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.Retryable;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public interface ViewDetails extends Retryable {

    void render(ViewStateDetails viewStateDetails);

    MovieBasic getMovie();

    Observable<Object> onRetryComments();

    Observable<Object> onRetryAll();

    Observable<Object> errorDismissed();

    void error(short message);

}
