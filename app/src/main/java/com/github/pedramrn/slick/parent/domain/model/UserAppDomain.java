package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

@AutoValue
public abstract class UserAppDomain {
    public abstract String id();
    public abstract String name();
    public abstract String email();
    public abstract String providerId();
    // public abstract String avatar();

    public static UserAppDomain create(String id, String name, String email, String providerId) {
        return new AutoValue_UserAppDomain(id, name, email, providerId);
    }

    public static UserAppDomain create(@NonNull FirebaseUser fbu) {
        return new AutoValue_UserAppDomain(fbu.getUid(), fbu.getDisplayName(), fbu.getEmail(), fbu.getProviderId());
    }

}
