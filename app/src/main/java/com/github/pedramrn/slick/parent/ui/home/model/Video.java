package com.github.pedramrn.slick.parent.ui.home.model;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

@AutoValue
public abstract class Video {
    public abstract String type();
    public abstract String key();
    public abstract String name();

    public static Video create(String type, String key, String name) {
        return new AutoValue_Video(type, key, name);
    }
}
