package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.xwray.groupie.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */

public interface ViewBoxOffice {
    void update(List<Item> items);

    Observable<Object> onRetry();
}
