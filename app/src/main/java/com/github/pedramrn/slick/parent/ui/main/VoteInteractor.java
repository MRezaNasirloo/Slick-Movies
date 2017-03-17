package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.datasource.database.VoteRepository;
import com.github.pedramrn.slick.parent.library.Interactor;
import com.github.pedramrn.slick.parent.library.Middleware;
import com.github.pedramrn.slick.parent.library.intractor.InteractorCompletable;
import com.github.pedramrn.slick.parent.library.middleware.Callback;
import com.github.pedramrn.slick.parent.library.middleware.RequestData;
import com.github.pedramrn.slick.parent.library.middleware.RequestStack;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

@Interactor(middleware = @Middleware(value = MiddlewareHasLoggedIn.class, methods = {"execute", "doStuff"}))
public class VoteInteractor extends InteractorCompletable<String> {

    private static final String TAG = VoteInteractor.class.getSimpleName();

    private final VoteRepository voteRepository;

    protected final MiddlewareHasLoggedIn middlewareHasLoggedIn;
    private final RouterLogin routerLogin;
    private final RouterCart routerCart;
    private final RouterVote routerVote;
    private final MiddlewareNoOp middlewareNoOp;
    RequestStack routerStack;

    @Inject
    public VoteInteractor(VoteRepository voteRepository, MiddlewareHasLoggedIn middlewareHasLoggedIn,
                          RouterLogin routerLogin, RouterCart routerCart, RouterVoteSlick routerVote, MiddlewareNoOp middlewareNoOp) {
        this.voteRepository = voteRepository;
        this.middlewareHasLoggedIn = middlewareHasLoggedIn;
        this.routerLogin = routerLogin;
        this.routerCart = routerCart;
        this.routerVote = routerVote;
        this.middlewareNoOp = middlewareNoOp;
        this.routerStack = RequestStack.getInstance();
    }

    @Override
    @Middleware(MiddlewareHasLoggedIn.class)
    public Completable execute(String s) {
        return routerVote.deleteVote(s);
    }

    @Middleware(MiddlewareHasLoggedIn.class)
    public Observable<String> voteDown(final RequestData req) {
        return routerVote.voteDown("1");
    }

    public String voteUp(RequestData data, Callback<String> callback) {
        return routerVote.voteUp(data, callback);
    }

    /*public void voteUp(RequestData data, Callback<String> callback) {
        final Request<String> request = new Request<String>() {
            @Override
            public String letItPass(RequestData data) {
                voteUp(data);
                return null;
            }
        };
        request.with(data)
                .through(middlewareHasLoggedIn, middlewareNoOp)
                .destination(callback);
        routerStack.push(request);
        routerStack.continueLastRequest();
    }*/


    @Middleware(MiddlewareHasLoggedIn.class)
    public String navigate(RequestData data) {
        return voteRepository.voteUp("id");
    }

}
