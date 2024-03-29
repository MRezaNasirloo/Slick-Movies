package com.github.pedramrn.slick.parent.ui.auth;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;

import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewAuth {
    @NonNull
    Observable<String> signInWithGoogle();

    @NonNull
    Observable<Object> signOut();

    // void showSignInDialog();

    @NonNull
    Observable<GugleSignInResult> result();

    void userSignedIn(UserApp user);
    void userSignedOut();

    void showLoading(boolean loading);

    void renderError(@Nullable Throwable error);
}
