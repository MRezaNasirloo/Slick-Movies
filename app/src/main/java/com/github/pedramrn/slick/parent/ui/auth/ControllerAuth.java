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
import com.github.pedramrn.slick.parent.exception.NotImplementedException;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.SnackbarManager;
import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.jakewharton.rxbinding2.view.RxView;
import com.mrezanasirloo.slick.Presenter;
import com.mrezanasirloo.slick.middleware.RequestStack;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerAuth extends ControllerBase implements ViewAuth {

    private static final String MANAGED = "MANAGED";
    private static final String PARENT_ID = "PARENT_ID";
    private static final int RC_SIGN_IN = 123;

    @Inject
    Provider<PresenterAuth> provider;
    @Presenter
    PresenterAuth presenter;

    private PublishSubject<GugleSignInResult> streamResult = PublishSubject.create();
    private ControllerAuthBinding binding;
    private boolean status;
    private final boolean managed;
    private final String parentId;

    public ControllerAuth(boolean managed, String parentId) {
        this(new BundleBuilder(new Bundle())
                .putBoolean(MANAGED, managed)
                .putString(PARENT_ID, parentId)
                .build());
    }

    public ControllerAuth(@NonNull Bundle args) {
        super(args);
        managed = args.getBoolean("MANAGED", false);
        parentId = args.getString(PARENT_ID);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.componentMain().inject(this);
        PresenterAuth_Slick.bind(this);
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
        if (managed) {
            Observable.just(1).delay(700, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        RequestStack.getInstance().processLastRequest();
                        Controller parentController = getParentController();
                        if (parentController != null) {
                            parentController.getRouter().handleBack();
                        }
                    });
        }
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
        super.onDestroyView(view);
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

    private static final String TAG = ControllerAuth.class.getSimpleName();

    @NonNull
    @Override
    public SnackbarManager snackbarManager() {
        throw new NotImplementedException("Sorry!!!");
    }
}
