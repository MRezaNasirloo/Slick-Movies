package com.github.pedramrn.slick.parent.domain.model;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-18
 */

@AutoValue
public abstract class User {
    public abstract int getId();

    public abstract String getBio();

    public abstract boolean isAwesome();

    public abstract String getName();

    public static User create(int newId, String newBio, boolean newAwesome, String newName) {
        return new AutoValue_User(newId, newBio, newAwesome, newName);
    }
}
