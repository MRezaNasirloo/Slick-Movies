package com.github.pedramrn.slick.parent.ui.header;


import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.xwray.groupie.Item;

import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewHeader extends Retryable {

    MovieBasic movie();

    void update(Item header);

    void error(short message);

    @NonNull
    Observable<Object> errorDismissed();

    @NonNull
    Observable<Object> onRetry();

}
