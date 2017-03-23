package com.github.pedramrn.slick.parent.ui.main;

import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.library.middleware.Callback;
import com.github.pedramrn.slick.parent.library.middleware.Request;
import com.github.pedramrn.slick.parent.library.middleware.RequestData;
import com.github.pedramrn.slick.parent.library.middleware.RequestStack;
import com.github.pedramrn.slick.parent.library.router.SlickRouter;
import com.github.pedramrn.slick.parent.ui.login.ControllerLogin;

import javax.inject.Inject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public class RouterLogin extends SlickRouter {

    private final Router router;
    RequestStack routerStack = RequestStack.getInstance();


    @Inject
    public RouterLogin(Router router) {
        this.router = router;
    }

    @Override
    public void route() {
        // TODO: 2017-03-12 go to login page
        router.pushController(RouterTransaction.with(new ControllerLogin()));
    }

    public void route(Callback<Void> callback) {
        final Request<Void, RequestData> request = new Request<Void, RequestData>() {
            @Override
            public Void destination(RequestData data) {
                 route();
                return null;
            }
        };
        request.with(null)
                .through()
                .destinationCallback(callback);
        routerStack.push(request);
        routerStack.processLastRequest();
    }

    public void finish() {
        router.popCurrentController();
    }
}
