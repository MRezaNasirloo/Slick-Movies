package com.github.pedramrn.slick.parent.ui.auth.model;

import android.os.Parcelable;

import com.github.pedramrn.slick.parent.domain.model.UserAppDomain;
import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

@AutoValue
public abstract class UserApp implements Parcelable {
    public abstract String id();

    public abstract String name();

    public abstract String email();

    public abstract String avatar();

    public abstract String providerId();

    public static UserApp create(String id, String name, String email, String avatar, String providerId) {
        return new AutoValue_UserApp(id, name, email, avatar, providerId);
    }

    public static UserApp create(UserAppDomain uad) {
        return new AutoValue_UserApp(uad.id(), uad.name(), uad.email(), uad.avatar(), uad.providerId());
    }

    // public abstract String avatar();

}
