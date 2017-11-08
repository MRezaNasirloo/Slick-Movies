package com.github.pedramrn.slick.parent.datasource.network.repository;

import android.support.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-29
 */

public class RepositoryAuthImpl implements RepositoryAuth, FirebaseAuth.AuthStateListener {

    private PublishSubject<FirebaseAuth> authStateChangeListener;

    @Inject
    public RepositoryAuthImpl() {
        authStateChangeListener = PublishSubject.create();
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

    @Override
    public Observable<FirebaseState> currentUser() {
        return Observable.create(emitter -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (!emitter.isDisposed() && auth.getCurrentUser() != null) {
                emitter.onNext(FirebaseState.create(auth));
                emitter.onComplete();
            } else if (!emitter.isDisposed()) {
                emitter.onError(new Throwable("User has not signed in yet."));
            }
        });
    }

    @Override
    public Observable<FirebaseState> signInGoogle(String idToken) {
        return Observable.create(emitter -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            /*if (!emitter.isDisposed()) {
                emitter.onNext(FirebaseState.create(auth));
            }*/
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            auth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
                if (!emitter.isDisposed()) {
                    emitter.onNext(FirebaseState.create(FirebaseAuth.getInstance()));
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
    public Observable<FirebaseState> signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        return authStateChangeListener
                .map(firebaseAuth -> FirebaseState.create(firebaseAuth, firebaseAuth.getCurrentUser()))
                .take(1);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        authStateChangeListener.onNext(firebaseAuth);
    }
}
