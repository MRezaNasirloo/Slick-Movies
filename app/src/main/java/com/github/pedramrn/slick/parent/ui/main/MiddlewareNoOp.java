package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.library.middleware.Middleware;
import com.github.pedramrn.slick.parent.library.middleware.Request;
import com.github.pedramrn.slick.parent.library.middleware.RequestData;
import com.github.pedramrn.slick.parent.ui.App;

import javax.inject.Inject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public class MiddlewareNoOp implements Middleware {

    private final RouterLogin router;
//    private final SnackbarLogin snackbarLogin;

    @Inject
    public MiddlewareNoOp(/*LoginRepository repository*/ RouterLogin router/*, SnackbarLogin snackbarLogin*/) {

        this.router = router;
//        this.snackbarLogin = snackbarLogin;
    }


    private boolean loggedIn() {
        /*return repository.HasLoggedIn()*/
        return true;
    }

    @Override
    public boolean handle(RequestData date) {
//        request.next(date);
        return true;
    }
}
