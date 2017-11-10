package com.github.pedramrn.slick.parent.ui.auth;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.UserAppDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.auth.model.GugleSignInResult;
import com.github.pedramrn.slick.parent.ui.auth.model.UserApp;
import com.github.pedramrn.slick.parent.ui.auth.router.RouterAuthImplSlick;
import com.github.pedramrn.slick.parent.ui.auth.state.SignedIn;
import com.github.pedramrn.slick.parent.ui.auth.state.SignedInLoading;
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
            RouterAuthImplSlick routerAuth,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main) {
        super(main, io);
        this.routerAuth = routerAuth;
    }

    @Override
    protected void start(ViewAuth view) {
        Observable<Object> commandSignInAnon = command(ViewAuth::signInAnonymously);
        Observable<String> commandSignInGoogle = command(ViewAuth::signInWithGoogle);
        Observable<Object> commandSignOut = command(ViewAuth::signOut);
        Observable<GugleSignInResult> commandResult = command(ViewAuth::result);

/*
        Observable<PartialViewState<ViewStateAuth>> commandSignInAnon = signInAnon.flatMap(o -> routerAuth.signInAnonymously())
                .map(UserApp::create)
                .map(LoggedIn::new);
*/
        // TODO: 2017-11-09 don't do it this way, it should be single purpose, don't mess things up, SOLID
        //Load sign in state on start up, then enable or disable the login button (Show  or Hide)
        Observable<PartialViewState<ViewStateAuth>> signInState = Observable.never().startWith(1)
                .flatMap(o -> routerAuth.currentFirebaseUser()
                        .map((Function<UserAppDomain, PartialViewState<ViewStateAuth>>) uad -> new SignedIn(UserApp.create(uad)))
                        .doOnError(Throwable::printStackTrace)
                        .onErrorReturnItem(new SignedOut())
                )
                .subscribeOn(io);

        /*Observable<PartialViewState<ViewStateAuth>> signOutState = Observable.never().startWith(1)
                .flatMap(o -> routerAuth.firebaseUserSignInStateStream()
                        .map((Function<Boolean, PartialViewState<ViewStateAuth>>) aBoolean -> new SignedIn())
                        .doOnError(Throwable::printStackTrace)
                        .onErrorReturnItem(new SignedOut())
                )
                .subscribeOn(io);*/


        Observable<PartialViewState<ViewStateAuth>> signInGoogle = commandSignInGoogle
                .flatMap(s -> routerAuth.signInGoogleAccount(s)
                        .map((Function<Object, PartialViewState<ViewStateAuth>>) uad -> new SignedInLoading(true))
                        .doOnError(Throwable::printStackTrace)
                        .startWith(new SignedInLoading(true))
                        // .onErrorReturnItem(new SignedOut())
                )
                .subscribeOn(io);



        Observable<PartialViewState<ViewStateAuth>> signOut = commandSignOut
                .flatMap(o -> routerAuth.signOut()
                        .map((Function<Object, PartialViewState<ViewStateAuth>>) usa -> new SignedOut())
                        .doOnError(Throwable::printStackTrace)
                )
                .subscribeOn(io);

        Observable<PartialViewState<ViewStateAuth>> firebaseSignedIn = commandResult.doOnNext(result -> Log.e(TAG, result.toString()))
                .filter(GugleSignInResult::isSuccess)
                .flatMap(result -> routerAuth.signInFirebaseWithGoogleAccount(result.idToken())
                        .map(UserApp::create)
                        .map((Function<UserApp, PartialViewState<ViewStateAuth>>) SignedIn::new)
                        .doOnError(Throwable::printStackTrace)
                )
                .subscribeOn(io);

        reduce(ViewStateAuth.builder().loading(false).build(), merge(signInGoogle, signOut, firebaseSignedIn, signInState)).subscribe(this);
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
