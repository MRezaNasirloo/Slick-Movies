package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.UserAppDomain;
import com.github.pedramrn.slick.parent.domain.model.UserStateAppDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-29
 */

public interface RouterAuth {
    Observable<UserAppDomain> signInAnonymously();

    Observable<UserStateAppDomain> signInFirebaseWithGoogleAccount(String idToken);

    Observable<Object> signInGoogleAccount();

    Observable<UserStateAppDomain> currentFirebaseUser();

    Observable<String> currentGoogleUser();

    Observable<UserStateAppDomain> signOut();

}
