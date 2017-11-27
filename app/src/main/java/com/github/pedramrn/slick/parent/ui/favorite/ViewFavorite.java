package com.github.pedramrn.slick.parent.ui.favorite;


import com.xwray.groupie.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewFavorite {

    void updateFavorites(List<Item> favorites);

    Observable<Object> triggerRefresh();
}
