package com.github.pedramrn.slick.parent.ui.people.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-19
 */

@AutoValue
public abstract class Person implements Parcelable {
    public abstract int id();

    public abstract String name();

    @Nullable
    protected abstract String profilePicId();

    public String profileThumbnail() {
        if (profilePicId() == null) return null;
        return "http://image.tmdb.org/t/p/w92" + profilePicId();
    }

    public String profileMedium() {
        if (profilePicId() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + profilePicId();
    }

    public String profileOriginal() {
        if (profilePicId() == null) return null;
        return "http://image.tmdb.org/t/p/original" + profilePicId();
    }

    public static Person create(int id, String name, String profilePicId) {
        return builder()
                .id(id)
                .name(name)
                .profilePicId(profilePicId)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Person.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(int id);

        public abstract Builder profilePicId(String profilePicId);

        public abstract Builder name(String name);

        public abstract Person build();
    }
}
