package com.github.pedramrn.slick.parent.ui.auth.router;

import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuth;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuthImpl;
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

    @Inject
    public RouterAuthImpl(RepositoryAuthImpl repositoryAuth) {
        this.repositoryAuth = repositoryAuth;
    }

    @Override
    public Observable<UserAppDomain> signInAnonymously() {
        return repositoryAuth.signInAnonymously()
                .map(fu -> UserAppDomain.create(fu.getUid(), fu.getDisplayName(), fu.getEmail(), fu.getProviderId()));
    }

    @Override
    public Observable<UserStateAppDomain> signInGoogle(String idToken) {
        return repositoryAuth.signInGoogle(idToken).map(firebaseState -> {
            FirebaseUser fbu = firebaseState.firebaseUser();
            if (fbu != null) {
                return UserStateAppDomain.create(UserAppDomain.create(fbu));
            } else {
                return UserStateAppDomain.create(null);
            }
        });
    }

    @Override
    public Observable<UserStateAppDomain> currentUser() {
        return repositoryAuth.currentUser().map(firebaseState -> {
            FirebaseUser fbu = firebaseState.firebaseUser();
            if (fbu != null) {
                return UserStateAppDomain.create(UserAppDomain.create(fbu));
            } else {
                return UserStateAppDomain.create(null);
            }
        });
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
