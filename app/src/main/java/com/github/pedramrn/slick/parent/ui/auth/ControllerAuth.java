package com.github.pedramrn.slick.parent.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerAuthBinding;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.slick.Presenter;
import com.github.slick.middleware.RequestStack;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.jakewharton.rxbinding2.view.RxView;

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

    private final int RC_SIGN_IN = 123;
    private PublishSubject<GugleSignInResult> streamResult = PublishSubject.create();
    private ControllerAuthBinding binding;
    private boolean status;
    private boolean managed;

    public ControllerAuth(boolean managed) {
        this(new BundleBuilder(new Bundle()).putBoolean("MANAGED", managed).build());
    }

    public ControllerAuth(@Nullable Bundle args) {
        super(args);
        managed = args != null && args.getBoolean("MANAGED", false);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        ControllerAuth_Slick.bind(this);
        binding = ControllerAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public Observable<String> signInWithGoogle() {
        return RxView.clicks(binding.buttonGoogle).throttleFirst(1, TimeUnit.SECONDS).map(o -> getInstanceId());
    }

    @Override
    public Observable<Object> signOut() {
        return RxView.clicks(binding.buttonSignOut).throttleFirst(1, TimeUnit.SECONDS);
    }

    @Override
    public Observable<GugleSignInResult> result() {
        return streamResult;
    }

    @Override
    public void userSignedIn(UserApp user) {
        status = true;
        binding.buttonGoogle.setVisibility(View.GONE);
        binding.buttonSignOut.setVisibility(View.VISIBLE);
        binding.textViewProfileInfo.setText(user.name());
        binding.textViewProfileInfo.setVisibility(View.VISIBLE);
        binding.imageViewAvatarUser.load(user.avatar());
        binding.imageViewAvatarUser.setVisibility(View.VISIBLE);

    }

    @Override
    public void userSignedOut() {
        status = false;
        binding.buttonGoogle.setVisibility(View.VISIBLE);
        binding.buttonSignOut.setVisibility(View.GONE);
        binding.imageViewAvatarUser.setVisibility(View.GONE);
        binding.imageViewAvatarUser.setImageResource(R.drawable.circle);
        binding.textViewProfileInfo.setText(R.string.sign_in_help);
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        binding = null;
    }

    @Override
    public void showLoading(boolean loading) {
        binding.progressBarSignIn.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void renderError(@Nullable Throwable error) {
        if (error != null) {
            binding.textViewProfileInfo.setText(error.getMessage());
        }
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

    @Override
    public boolean handleBack() {
        if (managed) {
            if (status) {
                getRouter().popController(this);
                return true;
            } else {
                RequestStack.getInstance().handleBack();
                return super.handleBack();
            }

        } else {
            return false;
        }
    }

    private static final String TAG = ControllerAuth.class.getSimpleName();
}
