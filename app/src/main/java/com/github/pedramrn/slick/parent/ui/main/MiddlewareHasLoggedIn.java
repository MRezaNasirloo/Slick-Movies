package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.library.middleware.IRequest;
import com.github.pedramrn.slick.parent.library.middleware.Middleware;
import com.github.pedramrn.slick.parent.library.middleware.RequestData;
import com.github.pedramrn.slick.parent.ui.App;

import javax.inject.Inject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public class MiddlewareHasLoggedIn implements Middleware {

    private static final String TAG = MiddlewareHasLoggedIn.class.getSimpleName();

    private final RouterLogin router;
//    private final SnackbarLogin snackbarLogin;

    @Inject
    public MiddlewareHasLoggedIn(/*LoginRepository repository*/ RouterLogin router/*, SnackbarLogin snackbarLogin*/) {

        this.router = router;
//        this.snackbarLogin = snackbarLogin;
    }



    private boolean loggedIn() {
        /*return repository.HasLoggedIn()*/
        return App.loggedIn;
    }

    @Override
    public void handle(IRequest request, RequestData date) {
        if (loggedIn()) {
//            request.next(date);
            request.next();
            return;
        }
        router.route(null);
//        dialog.show();
//        snackbar.show();
//        errorMechanism.display();
//        return false;
    }
}
