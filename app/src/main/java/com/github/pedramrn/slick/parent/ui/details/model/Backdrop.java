package com.github.pedramrn.slick.parent.ui.details.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Backdrop {

    @Nullable
    protected abstract String backdropPath();


    @Nullable
    public String backdropThumbnail() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + backdropPath();
    }

    @Nullable
    public String backdropOriginal() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/original" + backdropPath();
    }

    public static Backdrop create(String backdropPath) {
        return new AutoValue_Backdrop(backdropPath);
    }


}
