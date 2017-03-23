package com.github.pedramrn.slick.parent.ui.main;

import android.support.annotation.Nullable;
import android.util.Log;

import com.github.pedramrn.slick.parent.datasource.database.VoteRepository;
import com.github.pedramrn.slick.parent.library.middleware.Callback;
import com.github.pedramrn.slick.parent.library.middleware.Request;
import com.github.pedramrn.slick.parent.library.middleware.RequestData;
import com.github.pedramrn.slick.parent.library.middleware.RequestStack;
import com.github.pedramrn.slick.parent.library.middleware.RxRequestObservable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-15
 */

public class RouterVoteSlick<B> extends RouterVote<B> {


    private final MiddlewareHasLoggedIn middlewareHasLoggedIn;
    private final MiddlewareNoOp middlewareNoOp;
    private RequestStack requestStack = RequestStack.getInstance();

    @Inject
    public RouterVoteSlick(VoteRepository repository, MiddlewareHasLoggedIn middlewareHasLoggedIn,
                           MiddlewareNoOp middlewareNoOp) {
        super(repository);
        this.middlewareHasLoggedIn = middlewareHasLoggedIn;
        this.middlewareNoOp = middlewareNoOp;
    }


    /*@Override
    @Nullable
    public String voteUp(RequestData data, Callback<String> callback) {
        final Request<String, RequestData> request = new Request<String, RequestData>() {
            @Override
            public String destination(RequestData data) {
                return RouterVoteSlick.super.voteUp(data, null);
            }
        };
        request.with(data)
                .through(middlewareHasLoggedIn, middlewareNoOp)
                .destinationCallback(callback);
        requestStack.push(request);
        requestStack.processLastRequest();
        return null;
    }*/

    private static final String TAG = RouterVoteSlick.class.getSimpleName();

    @Override
    public Observable<String> voteDown(String id) {
        RxRequestObservable<Observable<String>, String, String>
                request = new RxRequestObservable<Observable<String>, String, String>() {
            @Override
            public Observable<String> letItPass(String data) {
                Log.d(TAG, "letItPass() called");
                return RouterVoteSlick.super.voteDown(data);
            }
        };
        final PublishSubject<String> subject = PublishSubject.create();
        request.with(id)
                .through(middlewareHasLoggedIn, middlewareNoOp)
                .destination(subject);
        requestStack.push(request);

        requestStack.processLastRequest();
        return subject;
    }
}
