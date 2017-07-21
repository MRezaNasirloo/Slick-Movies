package com.github.pedramrn.slick.parent.ui.home.item;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-21
 */
public interface ItemMovie {
    @Nullable
    MovieBasic movie();

    @Nullable
    String transitionName();
}
