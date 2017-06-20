package com.github.pedramrn.slick.parent.domain.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

@AutoValue
public abstract class VideoDomain {
    public abstract String type();

    public abstract String key();

    public abstract String name();

    public static VideoDomain create(String type, String key, String name) {
        return new AutoValue_VideoDomain(type, key, name);
    }

}
