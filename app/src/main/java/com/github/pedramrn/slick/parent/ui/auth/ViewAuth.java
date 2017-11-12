package com.github.pedramrn.slick.parent.ui.auth;


import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;

import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewAuth {
    Observable<String> signInWithGoogle();

    Observable<Object> signOut();

    // void showSignInDialog();

    Observable<GugleSignInResult> result();

    void userSignedIn(UserApp user);
    void userSignedOut();
}
