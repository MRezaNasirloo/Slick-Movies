package com.github.pedramrn.slick.parent.datasource.network.repository;

import android.app.Activity;
import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.pedramrn.slick.parent.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-08
 */
public class RepositoryGoogleAuthImpl implements DefaultLifecycleObserver, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private WeakReference<LifecycleOwner> ownerWeakReference;
    private GoogleApiClient googleApiClient;
    private final Context context;

    @Inject
    public RepositoryGoogleAuthImpl(Context context) {
        this.context = context;
    }

    public Observable<String> currentUser() {
        return Observable.create(emitter -> Auth.GoogleSignInApi.silentSignIn(googleApiClient).setResultCallback(result -> {
            if (!emitter.isDisposed() && result.isSuccess() && result.getSignInAccount() != null &&
                    result.getSignInAccount().getIdToken() != null) {
                GoogleSignInAccount signInAccount = result.getSignInAccount();
                emitter.onNext(signInAccount.getIdToken());
                emitter.onComplete();
            } else {
                emitter.onError(new Throwable(result.getStatus().getStatusMessage()));
            }
        }));
    }

    public Observable<Object> signIn() {
        return Observable.create(e -> {
            if (googleApiClient == null) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getResources().getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                googleApiClient = new GoogleApiClient.Builder(context)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
            }
            // TODO: 2017-11-08 catch has
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            ((Activity) ownerWeakReference.get()).startActivityForResult(signInIntent, 123);
        });
    }


    public Observable<Object> signOut() {
        return Observable.create(emitter -> Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
            if (!emitter.isDisposed() && status.isSuccess()) {
                emitter.onNext(0);
                emitter.onComplete();
                googleApiClient.clearDefaultAccountAndReconnect();
            } else if (!emitter.isDisposed() && !status.isSuccess()) {
                emitter.onError(new Throwable(status.getStatusMessage()));
            }
        }, 5, TimeUnit.SECONDS));
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        googleApiClient.connect();
        ownerWeakReference = new WeakReference<>(owner);
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        googleApiClient.disconnect();
        ownerWeakReference.clear();
        ownerWeakReference = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected() called with: bundle = [" + bundle + "]");
    }

    private static final String TAG = RepositoryGoogleAuthImpl.class.getSimpleName();

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended() called with: i = [" + i + "]");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed() called with: connectionResult = [" + connectionResult + "]");
    }
}

