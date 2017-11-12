package com.github.pedramrn.slick.parent.ui.auth;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.github.slick.OnDestroyListener;
import com.github.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-11
 */

public class AuthViewMinimal extends LinearLayout implements ViewAuth, OnDestroyListener {

    @Inject Provider<PresenterAuth> provider;
    @Presenter PresenterAuth presenter;

    public AuthViewMinimal(Context context) {
        super(context);
    }

    public AuthViewMinimal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AuthViewMinimal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AuthViewMinimal(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AuthViewMinimal_Slick.bind(this);
    }

    @Override
    public Observable<String> signInWithGoogle() {
        return null;
    }

    @Override
    public Observable<Object> signOut() {
        return null;
    }

    @Override
    public Observable<GugleSignInResult> result() {
        return null;
    }

    @Override
    public void userSignedIn(UserApp user) {

    }

    @Override
    public void userSignedOut() {

    }

    @Override
    public void onDestroy() {

    }
}
