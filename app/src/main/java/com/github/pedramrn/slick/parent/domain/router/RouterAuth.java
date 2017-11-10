package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.UserAppDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-29
 */

public interface RouterAuth {
    Observable<UserAppDomain> signInAnonymously();

    Observable<UserAppDomain> signInFirebaseWithGoogleAccount(String idToken);

    Observable<Object> signInGoogleAccount(String s);

    Observable<UserAppDomain> currentFirebaseUser();

    Observable<Boolean> firebaseUserSignInStateStream();

    Observable<String> currentGoogleUser();

    Observable<Object> signOut();

}
