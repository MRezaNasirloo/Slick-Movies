package com.github.pedramrn.slick.parent.ui.details;

import android.support.annotation.NonNull;

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

    @NonNull
    Observable<Object> onRetryAll();

    @NonNull
    Observable<Object> errorDismissed();

    void error(short message);

}
