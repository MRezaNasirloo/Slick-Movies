package com.github.pedramrn.slick.parent.datasource.network.repository;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observable;


/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-29
 */

public interface RepositoryAuth {
    Observable<FirebaseUser> signInAnonymously();
    Observable<FirebaseState> currentUser();

    Observable<FirebaseState> signInGoogle(String idToken);

    Observable<FirebaseState> signOut();
}
