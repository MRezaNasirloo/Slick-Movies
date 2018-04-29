package com.github.pedramrn.slick.parent.ui.middleware;

import com.github.pedramrn.slick.parent.domain.model.UserAppDomain;
import com.github.pedramrn.slick.parent.ui.Navigator2;
import com.github.pedramrn.slick.parent.ui.auth.router.RouterAuthImpl;
import com.github.pedramrn.slick.parent.ui.dialog.ControllerDialog;
import com.mrezanasirloo.slick.middleware.BundleSlick;
import com.mrezanasirloo.slick.middleware.Middleware;
import com.mrezanasirloo.slick.middleware.Request;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-03-29
 */

public class MiddlewareLogin extends Middleware {

    private final RouterAuthImpl routerAuth;
    private final Scheduler main;
    private final Scheduler io;
    private Disposable disposable;

    @Inject
    public MiddlewareLogin(RouterAuthImpl routerAuth, @Named("main") Scheduler main, @Named("io") Scheduler io) {
        this.routerAuth = routerAuth;
        this.main = main;
        this.io = io;
    }

    @Override
    public void handle(Request request, BundleSlick date) {
        routerAuth.currentFirebaseUser()
                // .retryUntil(() -> Navigator2.hasRouter())
                .subscribe(new Observer<UserAppDomain>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(UserAppDomain userAppDomain) {
                        request.next();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Navigator2.show(new ControllerDialog(), ControllerDialog.TAG);
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }
}
