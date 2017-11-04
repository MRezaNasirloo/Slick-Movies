package com.github.pedramrn.slick.parent.datasource.database.repository;

import com.github.pedramrn.slick.parent.domain.model.User;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-18
 */

public interface RepositoryUser {

    Observable<User> get(String name);

    Single<Integer> update(String bio, boolean awesome);

    Completable create(User user);


}
