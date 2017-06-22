package com.github.pedramrn.slick.parent.ui.popular;

import com.github.pedramrn.slick.parent.domain.model.User;
import com.github.pedramrn.slick.parent.domain.repository.RepositoryUser;
import com.github.pedramrn.slick.parent.datasource.database.RepositoryUserImpl;
import com.github.pedramrn.slick.parent.ui.upcoming.ViewUpComing;
import com.github.slick.SlickPresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class PresenterPopular extends SlickPresenter<ViewPopular> {

    private RepositoryUser repositoryUser;

    @Inject
    public PresenterPopular(/*RepositoryUserImpl repositoryUser*/) {
        // this.repositoryUser = repositoryUser;
    }


    public Observable<User> getUser(String name) {
        return repositoryUser
                .get(name)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    public Completable createUser(User user) {
        return repositoryUser
                .create(user)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    public Single<Integer> updateUser(String bio, boolean awesome) {
        return repositoryUser
                .update(bio, awesome)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }
}
