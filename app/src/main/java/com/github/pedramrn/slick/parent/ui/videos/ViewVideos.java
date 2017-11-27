package com.github.pedramrn.slick.parent.ui.videos;


import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.xwray.groupie.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewVideos {

    MovieBasic movie();

    void update(List<Item> videos);

    void error(short message);

    Observable<Object> onErrorDismissed();

    Observable<Object> onRetry();

}
