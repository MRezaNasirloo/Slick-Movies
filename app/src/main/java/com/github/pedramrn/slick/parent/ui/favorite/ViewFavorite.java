package com.github.pedramrn.slick.parent.ui.favorite;


import android.support.annotation.Nullable;

import com.xwray.groupie.Item;

import java.util.List;

/**
 * A simple View interface
 */
public interface ViewFavorite {

    void renderError(@Nullable Throwable throwable);

    void updateFavorites(List<Item> favorites);
}
