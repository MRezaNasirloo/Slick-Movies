package com.github.pedramrn.slick.parent.ui.auth;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.model.UserStateAppDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.github.pedramrn.slick.parent.ui.auth.router.RouterAuthImpl;
import com.github.pedramrn.slick.parent.ui.auth.state.GoogleSignedIn;
import com.github.pedramrn.slick.parent.ui.auth.state.SignInDialog;
import com.github.pedramrn.slick.parent.ui.auth.state.SignedIn;
import com.github.pedramrn.slick.parent.ui.auth.state.SignedOut;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * A simple Presenter
 */
public class PresenterAuth extends PresenterBase<ViewAuth, ViewStateAuth> {
    private static final String TAG = PresenterAuth.class.getSimpleName();
    private final RouterAuth routerAuth;

    @Inject
    public PresenterAuth(
            RouterAuthImpl routerAuth,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main) {
        super(main, io);
        this.routerAuth = routerAuth;
    }

    @Override
    protected void start(ViewAuth view) {
        Observable<Object> commandSignInAnon = command(ViewAuth::signInAnonymously);
        Observable<Object> commandSignInGoogle = command(ViewAuth::signInWithGoogle);
        Observable<Object> commandSignOut = command(ViewAuth::signOut);
        Observable<GugleSignInResult> commandResult = command(ViewAuth::result);

/*
        Observable<PartialViewState<ViewStateAuth>> commandSignInAnon = signInAnon.flatMap(o -> routerAuth.signInAnonymously())
                .map(UserApp::create)
                .map(LoggedIn::new);
*/
        Observable<PartialViewState<ViewStateAuth>> signInGoogle = commandSignInGoogle
                .flatMap(o -> routerAuth.currentUser())
                .map(userStateAppDomain -> {
                    if (userStateAppDomain.signedIn()) {
                        return new SignedIn(UserApp.create(userStateAppDomain.user()));
                    } else if (userStateAppDomain.showSignInDialog()) {
                        return new SignInDialog();
                    } else {
                        // FIXME: 2017-11-01 should not reach.
                        return null;
                    }
                }).subscribeOn(io);

        Observable<PartialViewState<ViewStateAuth>> signOut = commandSignOut
                .flatMap(o -> routerAuth.signOut())
                .filter(userStateAppDomain -> !userStateAppDomain.signedIn())
                .map((Function<UserStateAppDomain, PartialViewState<ViewStateAuth>>) userStateAppDomain -> new SignedOut())
                .subscribeOn(io);

        Observable<PartialViewState<ViewStateAuth>> changeShowDialogState = commandResult.map(GoogleSignedIn::new);
        Observable<PartialViewState<ViewStateAuth>> firebaseSignedIn = commandResult.filter(GugleSignInResult::isSuccess)
                .flatMap(result -> routerAuth.signInGoogle(result.idToken()))
                .filter(UserStateAppDomain::signedIn)
                .map(userStateAppDomain -> UserApp.create(userStateAppDomain.user()))
                .map((Function<UserApp, PartialViewState<ViewStateAuth>>) SignedIn::new)
                .subscribeOn(io);

        reduce(ViewStateAuth.builder().showSignInDialog(false).build(), merge(signInGoogle, signOut, changeShowDialogState, firebaseSignedIn)).subscribe(this);
    }


    @Override
    protected void render(@NonNull ViewStateAuth state, @NonNull ViewAuth view) {
        if (state.showSignInDialog()) {
            view.showSignInDialog();
        }
        if (state.user() != null) {
            view.userSignedIn(state.user());
        }
        Boolean signedOut = state.signedOut();
        if (signedOut != null && signedOut) {
            view.userSignedOut();
        }
    }
}
