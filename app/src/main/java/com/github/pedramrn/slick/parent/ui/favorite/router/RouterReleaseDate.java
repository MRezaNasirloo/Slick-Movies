package com.github.pedramrn.slick.parent.ui.favorite.router;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.datasource.database.repository.RepositoryReleaseDates;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuth;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuthImpl;
import com.github.pedramrn.slick.parent.ui.details.model.ReleaseDateModel;
import com.github.pedramrn.slick.parent.ui.middleware.MiddlewareLogin;
import com.mrezanasirloo.slick.Middleware;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-11-09
 */

public class RouterReleaseDate {

    private final RepositoryReleaseDates repositoryReleaseDare;
    private final RepositoryAuth repositoryAuth;

    @Inject
    public RouterReleaseDate(RepositoryReleaseDates repositoryReleaseDare, RepositoryAuthImpl repositoryAuth) {
        this.repositoryReleaseDare = repositoryReleaseDare;
        this.repositoryAuth = repositoryAuth;
    }

    @Middleware({MiddlewareLogin.class})
    public Completable add(@NonNull ReleaseDateModel releaseDateModel) {
        return repositoryAuth.currentUser()
                .flatMapCompletable(firebaseUser -> repositoryReleaseDare.addMovieReleaseDate(
                        firebaseUser.getUid(),
                        releaseDateModel
                ));
    }

    @Middleware(MiddlewareLogin.class)
    public Completable remove(@NonNull ReleaseDateModel releaseDateModel) {
        return repositoryAuth.currentUser()
                .flatMapCompletable(firebaseUser -> repositoryReleaseDare.removeMovieReleaseDate(
                        firebaseUser.getUid(),
                        releaseDateModel
                ));

    }

    @Middleware({MiddlewareLogin.class})
    public Observable<ReleaseDateModel> updateStream(@NonNull Integer tmdbId) {
        System.out.println("LOG_IT_RouterFavoriteImpl.updateStream: " + tmdbId);
        return repositoryAuth.currentUser()
                .flatMap(firebaseUser -> repositoryReleaseDare.updateStream(firebaseUser.getUid(), tmdbId));
    }
}
