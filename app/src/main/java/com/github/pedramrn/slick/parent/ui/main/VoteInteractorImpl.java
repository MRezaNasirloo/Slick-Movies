package com.github.pedramrn.slick.parent.ui.main;


import com.github.pedramrn.slick.parent.datasource.database.VoteRepository;
import com.github.pedramrn.slick.parent.library.Middleware;
import com.github.pedramrn.slick.parent.library.middleware.Callback;
import com.github.pedramrn.slick.parent.library.middleware.Request;
import com.github.pedramrn.slick.parent.library.middleware.RequestData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.Call;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-18
 */

public class VoteInteractorImpl extends VoteInteractor {
    public VoteInteractorImpl(VoteRepository voteRepository,
                              MiddlewareHasLoggedIn middlewareHasLoggedIn,
                              RouterLogin routerLogin, RouterCart routerCart,
                              RouterVoteSlick routerVote,
                              MiddlewareNoOp middlewareNoOp) {
        super(voteRepository, middlewareHasLoggedIn, routerLogin, routerCart, routerVote, middlewareNoOp);
    }

    @Override
    public Object execute(Object r) throws IllegalArgumentException {
        return super.execute(r);
    }

    public <R, T> List<R> dos(){
        return null;
    }

    @Middleware({MiddlewareNoOp.class})
    public <R extends T, T> List<R> doSomething(R r){
        return new ArrayList<>();
    }

    @Middleware({MiddlewareNoOp.class})
    public Observable<String> donotSomething(String r){
        return io.reactivex.Observable.just("foo");
    }

    /*@Override
    public String voteUp(RequestData data, Callback<String> callback) {
        final Request<String, RequestData> request = new Request<String, RequestData>(){
            @Override
            public String destination(RequestData data2) {
                return VoteInteractor.super.voteUp(data2 ,null);
            }
        };
        return super.voteUp(data, callback);
    }*/

    @Override
    public String voteUp(RequestData data, Callback callback) {
        final Request<String, RequestData> request = new Request<String, RequestData>(){
            @Override
            public String destination(RequestData data2) {
                return VoteInteractorImpl.super.voteUp(data2 ,null);
            }
        };
        return super.voteUp(data, callback);
    }
}
