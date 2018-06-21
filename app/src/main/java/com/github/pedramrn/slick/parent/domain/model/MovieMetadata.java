package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-10
 */
public interface MovieMetadata {

    Integer id();

    @Nullable
    String imdbId();

    @Nullable
    String title();

    @Nullable
    Integer year();
}
