package com.github.pedramrn.slick.parent.ui.main;

import android.util.Log;

import com.github.pedramrn.slick.parent.library.middleware.Callback;
import com.github.pedramrn.slick.parent.library.middleware.Middleware;
import com.github.pedramrn.slick.parent.library.middleware.Request;
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
    public boolean handle(RequestData date) {
        if (loggedIn()) {
//            request.next(date);
            return true;
        }
        router.route(null);
//        dialog.show();
//        snackbar.show();
//        errorMechanism.display();
        return false;
    }
}
