package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.datasource.database.VoteRepository;
import com.github.pedramrn.slick.parent.library.Middleware;
import com.github.pedramrn.slick.parent.library.middleware.Callback;
import com.github.pedramrn.slick.parent.library.middleware.Request;
import com.github.pedramrn.slick.parent.library.middleware.RequestData;
import com.github.pedramrn.slick.parent.library.middleware.RequestStack;
import com.github.pedramrn.slick.parent.ui.App;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-15
 */

public class RouterVote {

    private final VoteRepository repository;
    private final RequestStack requestStack = RequestStack.getInstance();

    @Inject
    public RouterVote(VoteRepository repository) {
        this.repository = repository;
    }

    @Middleware(MiddlewareHasLoggedIn.class)
    public String voteUp(RequestData data, Callback<String> callback) {
        return repository.voteUp("1");
    }

    public Observable<String> voteDown(String id) {
        return repository.voteDown(id);
    }


    public Completable deleteVote(String id){
        return repository.deleteVote(id);
    }
}
