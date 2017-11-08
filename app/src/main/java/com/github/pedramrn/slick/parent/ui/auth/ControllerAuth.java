package com.github.pedramrn.slick.parent.ui.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerAuthBinding;
import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.slick.Presenter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerAuth extends ControllerBase implements ViewAuth {

    @Inject
    Provider<PresenterAuth> provider;
    @Presenter
    PresenterAuth presenter;
    private Button buttonSignOut;
    private Button buttonAnonymous;
    private SignInButton buttonGoogle;
    private final int RC_SIGN_IN = 123;
    private PublishSubject<GugleSignInResult> streamResult = PublishSubject.create();
    private GoogleApiClient mGoogleApiClient;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        // TODO: 2017-07-22 Inject dependencies 
        App.componentMain().inject(this);
        ControllerAuth_Slick.bind(this);
        ControllerAuthBinding binding = ControllerAuthBinding.inflate(inflater, container, false);
        buttonAnonymous = binding.buttonAnonymous;
        buttonGoogle = binding.buttonGoogle;
        buttonSignOut = binding.buttonSignOut;
        return binding.getRoot();
    }

    @Override
    public Observable<Object> signInAnonymously() {
        return RxView.clicks(buttonAnonymous).throttleFirst(1, TimeUnit.SECONDS);
    }

    @Override
    public Observable<Object> signInWithGoogle() {
        return RxView.clicks(buttonGoogle).throttleFirst(1, TimeUnit.SECONDS);
    }

    @Override
    public Observable<Object> signOut() {
        return RxView.clicks(buttonSignOut).throttleFirst(1, TimeUnit.SECONDS);
    }

    /*@Override
    public void showSignInDialog() {
        *//*Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .build();*//*

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        *//* FragmentActivity *//**//* OnConnectionFailedListener *//*
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(((AppCompatActivity) getActivity()) *//* FragmentActivity *//*, null *//* OnConnectionFailedListener *//*)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        // Auth.GoogleSignInApi.signOut(mGoogleApiClient).await();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        // Auth.GoogleSignInApi.
        // mGoogleApiClient.clearDefaultAccountAndReconnect();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/

    @Override
    public Observable<GugleSignInResult> result() {
        return streamResult;
    }

    @Override
    public void userSignedIn(UserApp user) {
        Toast.makeText(getApplicationContext(), String.format(Locale.ENGLISH, "Name: %s\n id: %s", user.name(), user.id()), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void userSignedOut() {
        Toast.makeText(getApplicationContext(), "You've benn signed out.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess() && result.getSignInAccount() != null && result.getSignInAccount().getIdToken() != null) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                streamResult.onNext(GugleSignInResult.builder().isSuccess(true).idToken(account.getIdToken()).build());
            } else {
                streamResult.onNext(GugleSignInResult.builder().isSuccess(false).build());
            }
        }
    }

    private static final String TAG = ControllerAuth.class.getSimpleName();
}
