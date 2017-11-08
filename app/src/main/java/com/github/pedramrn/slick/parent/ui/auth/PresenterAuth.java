package com.github.pedramrn.slick.parent.ui.auth;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.model.UserStateAppDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.github.pedramrn.slick.parent.ui.auth.router.RouterAuthImpl;
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
        // TODO: 2017-11-09 don't do it this way, it should be single purpose, don't mess things up, SOLID
        //Load sign in state on start up, then enable or disable the login button (Show  or Hide)
        Observable<PartialViewState<ViewStateAuth>> signInGoogle = commandSignInGoogle.startWith(1)
                .flatMap(o -> routerAuth.currentFirebaseUser().filter(UserStateAppDomain::signedIn)
                        .map((Function<UserStateAppDomain, PartialViewState<ViewStateAuth>>) usa -> new SignedIn(UserApp.create(usa.user())))
                        .doOnError(Throwable::printStackTrace)
                        .onErrorReturnItem(new SignedOut())
                )
                .subscribeOn(io);

        Observable<PartialViewState<ViewStateAuth>> signOut = commandSignOut
                .flatMap(o -> routerAuth.signOut().filter(userStateAppDomain -> !userStateAppDomain.signedIn())
                        .map((Function<UserStateAppDomain, PartialViewState<ViewStateAuth>>) usa -> new SignedOut())
                        .doOnError(Throwable::printStackTrace)
                )
                .subscribeOn(io);

        Observable<PartialViewState<ViewStateAuth>> firebaseSignedIn = commandResult.filter(GugleSignInResult::isSuccess)
                .flatMap(result -> routerAuth.signInFirebaseWithGoogleAccount(result.idToken()).filter(UserStateAppDomain::signedIn)
                        .map(usa -> UserApp.create(usa.user()))
                        .map((Function<UserApp, PartialViewState<ViewStateAuth>>) SignedIn::new)
                        .doOnError(Throwable::printStackTrace)
                )
                .subscribeOn(io);

        reduce(ViewStateAuth.builder().build(), merge(signInGoogle, signOut, firebaseSignedIn)).subscribe(this);
    }


    @Override
    protected void render(@NonNull ViewStateAuth state, @NonNull ViewAuth view) {
        if (state.user() != null) {
            view.userSignedIn(state.user());
        }
        Boolean signedOut = state.signedOut();
        if (signedOut != null && signedOut) {
            view.userSignedOut();
        }
    }
}
