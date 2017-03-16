package com.github.pedramrn.slick.parent.ui.main;

import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.library.Middleware;
import com.github.pedramrn.slick.parent.library.middleware.RequestStack;
import com.github.pedramrn.slick.parent.library.router.SlickRouter;
import com.github.pedramrn.slick.parent.ui.App;
import com.github.pedramrn.slick.parent.ui.login.ControllerCart;

import javax.inject.Inject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

class RouterCart extends SlickRouter {

    private final Router router;
    RequestStack routerStack = RequestStack.getInstance();

//    private final RequestStack routerStack;

    @Inject
    public RouterCart(Router router) {
        this.router = router;
    }

    @Override
    @Middleware(MiddlewareHasLoggedIn.class)
    public void route() {
        // TODO: 2017-03-12 go to login page
        router.pushController(RouterTransaction.with(new ControllerCart()));
    }


}
