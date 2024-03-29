package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

@AutoValue
public abstract class UserComment implements Parcelable {
    public abstract String username();

    @Nullable
    public abstract String name();

    public abstract String id();

    public static Builder builder() {
        return new AutoValue_UserComment.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder username(String username);

        public abstract Builder name(String name);

        public abstract Builder id(String id);

        public abstract UserComment build();
    }

    // public abstract String avatar();

}
