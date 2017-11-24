package com.github.pedramrn.slick.parent.ui.auth.router;

import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuth;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuthImpl;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryGoogleAuthImpl;
import com.github.pedramrn.slick.parent.domain.model.UserAppDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.github.pedramrn.slick.parent.ui.middleware.MiddlewareInternetAccess;
import com.github.slick.Middleware;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-10-29
 */

public class RouterAuthImpl implements RouterAuth {

    private final RepositoryAuth repositoryAuth;
    private final RepositoryGoogleAuthImpl repositoryGoogleAuth;

    @Inject
    public RouterAuthImpl(RepositoryAuthImpl repositoryAuth, RepositoryGoogleAuthImpl repositoryGoogleAuth) {
        this.repositoryAuth = repositoryAuth;
        this.repositoryGoogleAuth = repositoryGoogleAuth;
    }

    @Override
    public Observable<UserAppDomain> signInAnonymously() {
        return repositoryAuth.signInAnonymously().map(UserAppDomain::create);
    }

    @Override
    public Observable<UserAppDomain> signInFirebaseWithGoogleAccount(String idToken) {
        return repositoryAuth.signInGoogle(idToken).map(UserAppDomain::create);
    }

    @Override
    @Middleware({MiddlewareInternetAccess.class})
    public Observable<Object> signInGoogleAccount(String s) {
        return repositoryGoogleAuth.signIn(s);
    }

    @Override
    public Observable<UserAppDomain> currentFirebaseUser() {
        return repositoryAuth.currentUser().map(UserAppDomain::create);
    }

    @Override
    public Observable<Boolean> firebaseUserSignInStateStream() {
        return repositoryAuth.userSignInStateStream();
    }

    @Override
    public Observable<String> currentGoogleUser() {
        return repositoryGoogleAuth.currentUser();
    }


    @Override
    public Observable<Object> signOut() {
        //if we boxOffice an emission it means the sign out has occurred
        return repositoryAuth.signOut().zipWith(repositoryGoogleAuth.signOut(), (o, o2) -> o);
    }
}
