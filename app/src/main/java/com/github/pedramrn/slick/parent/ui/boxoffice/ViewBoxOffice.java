package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.ui.error.ErrorMessageHandler;
import com.xwray.groupie.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */

public interface ViewBoxOffice extends ErrorMessageHandler {
    void update(List<Item> items);

    Observable<Integer> onLoadMore();
    Observable<Object> onRetry();

    Observable<Object> onErrorDismissed();

    int pageSize();
}
