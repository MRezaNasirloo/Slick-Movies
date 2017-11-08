package com.github.pedramrn.slick.parent.ui.auth.router;

import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuth;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuthImpl;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryGoogleAuthImpl;
import com.github.pedramrn.slick.parent.domain.model.UserAppDomain;
import com.github.pedramrn.slick.parent.domain.model.UserStateAppDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.google.firebase.auth.FirebaseUser;

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
        return repositoryAuth.signInAnonymously()
                .map(fu -> UserAppDomain.create(fu.getUid(), fu.getDisplayName(), fu.getEmail(), fu.getProviderId()));
    }

    @Override
    public Observable<UserStateAppDomain> signInFirebaseWithGoogleAccount(String idToken) {
        return repositoryAuth.signInGoogle(idToken).map(firebaseState -> {
            FirebaseUser fbu = firebaseState.firebaseUser();
            return UserStateAppDomain.create(UserAppDomain.create(fbu));
        });
    }

    @Override
    public Observable<Object> signInGoogleAccount() {
        return repositoryGoogleAuth.signIn();
    }

    @Override
    public Observable<UserStateAppDomain> currentFirebaseUser() {
        return repositoryAuth.currentUser().flatMap(firebaseState -> {
            FirebaseUser fbu = firebaseState.firebaseUser();
            if (fbu != null) {
                return Observable.just(UserStateAppDomain.create(UserAppDomain.create(fbu)));
            } else {
                return Observable.error(new Throwable());
            }
        });
    }

    @Override
    public Observable<String> currentGoogleUser() {
        return repositoryGoogleAuth.currentUser();
    }


    @Override
    public Observable<UserStateAppDomain> signOut() {
        //we get the result from
        return repositoryAuth.signOut().map(firebaseState -> {
            FirebaseUser fbu = firebaseState.firebaseUser();
            if (fbu != null) {
                return UserStateAppDomain.create(UserAppDomain.create(fbu));
            } else {
                return UserStateAppDomain.create(null);
            }
        });
    }
}
