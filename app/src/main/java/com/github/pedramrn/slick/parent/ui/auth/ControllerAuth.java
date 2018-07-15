package com.github.pedramrn.slick.parent.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ControllerAuthBinding;
import com.github.pedramrn.slick.parent.exception.NotImplementedException;
import com.github.pedramrn.slick.parent.ui.ActivityMain;
import com.github.pedramrn.slick.parent.ui.BundleBuilder;
import com.github.pedramrn.slick.parent.ui.SnackbarManager;
import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.github.pedramrn.slick.parent.ui.home.FragmentBase;
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
public class ControllerAuth extends FragmentBase implements ViewAuth {

    private static final String MANAGED = "MANAGED";
    private static final int RC_SIGN_IN = 123;

    @Inject
    Provider<PresenterAuth> provider;
    @Presenter
    PresenterAuth presenter;

    private PublishSubject<GugleSignInResult> streamResult = PublishSubject.create();
    private ControllerAuthBinding binding;
    private boolean managed;

    public static Fragment newInstance(boolean managed) {
        ControllerAuth fragment = new ControllerAuth();
        Bundle bundle = new BundleBuilder(new Bundle())
                .putBoolean(MANAGED, managed)
                .build();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managed = getArguments().getBoolean("MANAGED", false);
        if (!managed) ((ActivityMain) getActivity()).setLogInFragment(this);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        App.componentMain().inject(this);
        PresenterAuth_Slick.bind(this);
        binding = ControllerAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @NonNull
    @Override
    public Observable<String> signInWithGoogle() {
        return RxView.clicks(binding.buttonGoogle).throttleFirst(1, TimeUnit.SECONDS).map(o -> getInstanceId());
    }

    @NonNull
    @Override
    public Observable<Object> signOut() {
        return RxView.clicks(binding.buttonSignOut).throttleFirst(1, TimeUnit.SECONDS);
    }

    @NonNull
    @Override
    public Observable<GugleSignInResult> result() {
        return streamResult;
    }

    @Override
    public void userSignedIn(UserApp user) {
        binding.buttonGoogle.setVisibility(View.GONE);
        binding.buttonSignOut.setVisibility(View.VISIBLE);
        binding.textViewProfileInfo.setText(user.name());
        binding.textViewProfileInfo.setVisibility(View.VISIBLE);
        binding.imageViewAvatarUser.load(user.avatar());
        binding.imageViewAvatarUser.setVisibility(View.VISIBLE);
        if (managed) {
            add(Observable.just(1).delay(700, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ignored -> {
                        RequestStack.getInstance().processLastRequest();
                        FragmentActivity activity = getActivity();
                        if (activity != null) activity.onBackPressed();
                    }));
        }
    }

    @Override
    public void userSignedOut() {
        binding.buttonGoogle.setVisibility(View.VISIBLE);
        binding.buttonSignOut.setVisibility(View.GONE);
        binding.imageViewAvatarUser.setVisibility(View.GONE);
        binding.imageViewAvatarUser.setImageResource(R.drawable.circle);
        binding.textViewProfileInfo.setText(R.string.sign_in_help);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
            if (result != null &&
                    result.isSuccess() &&
                    result.getSignInAccount() != null &&
                    result.getSignInAccount().getIdToken() != null) {
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
