package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

@AutoValue
public abstract class UserStateAppDomain {
    @Nullable
    public abstract UserAppDomain user();

    /**
     * @return true if the user has signed in or false if user has singed out.
     */
    public abstract boolean signedIn();

    public abstract boolean showSignInDialog();

    public static UserStateAppDomain create(@Nullable UserAppDomain user) {
        return new AutoValue_UserStateAppDomain(user, user != null, user == null);
    }

}
