package com.github.pedramrn.slick.parent.datasource.network.repository;

import android.support.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-29
 */

public class RepositoryAuthImpl implements RepositoryAuth, FirebaseAuth.AuthStateListener {

    private BehaviorSubject<FirebaseAuth> authStateChangeListener;

    @Inject
    public RepositoryAuthImpl() {
        authStateChangeListener = BehaviorSubject.create();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(this);
    }

    @Override
    public Observable<FirebaseUser> signInAnonymously() {
        return Observable.create(emitter -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser == null) {
                auth.signInAnonymously().addOnSuccessListener(authResult -> {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(authResult.getUser());
                    }
                }).addOnFailureListener(e -> {
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }).addOnCompleteListener(task -> {
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                });
            }
        });
    }

    /**
     * @return true if signed in, false if signed out
     */
    @Override
    public Observable<Boolean> userSignInStateStream(){
        return this.authStateChangeListener.map(firebaseAuth -> firebaseAuth.getCurrentUser() != null);
    }

    @Override
    public Observable<FirebaseUser> currentUser() {
        return Observable.create(emitter -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (!emitter.isDisposed() && auth.getCurrentUser() != null) {
                emitter.onNext(auth.getCurrentUser());
                emitter.onComplete();
            } else if (!emitter.isDisposed()) {
                emitter.onError(new Throwable("User has not signed in yet."));
            }
        });
    }

    @Override
    public Observable<FirebaseUser> signInGoogle(String idToken) {
        return Observable.create(emitter -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            /*if (!emitter.isDisposed()) {
                emitter.onNext(FirebaseState.create(auth));
            }*/
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            auth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
                if (!emitter.isDisposed()) {
                    emitter.onNext(FirebaseAuth.getInstance().getCurrentUser());
                }
            }).addOnFailureListener(e -> {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }).addOnCompleteListener(task -> {
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            });

        });

    }

    private static final String TAG = RepositoryAuthImpl.class.getSimpleName();

    @Override
    public Observable<Object> signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        return authStateChangeListener
                .filter(firebaseAuth -> firebaseAuth.getCurrentUser() == null)
                .cast(Object.class)
                .take(1);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        authStateChangeListener.onNext(firebaseAuth);
    }
}
