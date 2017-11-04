package com.github.pedramrn.slick.parent.datasource.network.repository;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-01
 */

@AutoValue
public abstract class FirebaseState {
    public abstract FirebaseAuth firebaseAuth();
    @Nullable
    public abstract FirebaseUser firebaseUser();

    public abstract boolean showSignInDialog();

    public static FirebaseState create(FirebaseAuth firebaseAuth, FirebaseUser firebaseUser, boolean showSignInDialog) {
        return new AutoValue_FirebaseState(firebaseAuth, firebaseUser, showSignInDialog);
    }


    public static FirebaseState create(FirebaseAuth firebaseAuth) {
        return new AutoValue_FirebaseState(firebaseAuth, firebaseAuth.getCurrentUser(), firebaseAuth.getCurrentUser() == null);
    }
}
