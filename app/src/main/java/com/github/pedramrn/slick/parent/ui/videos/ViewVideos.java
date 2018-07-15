package com.github.pedramrn.slick.parent.ui.videos;


import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.xwray.groupie.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewVideos extends Retryable {

    @NonNull
    String viewType();

    @NonNull
    Observable<MovieBasic> movieObservable();

    void update(@NonNull List<Item> videos);

    void error(short message);

    @NonNull
    Observable<Object> errorDismissed();

    @NonNull
    Observable<Object> onRetry();

}
