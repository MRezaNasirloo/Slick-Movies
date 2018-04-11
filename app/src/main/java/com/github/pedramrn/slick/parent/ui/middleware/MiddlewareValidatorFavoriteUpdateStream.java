package com.github.pedramrn.slick.parent.ui.middleware;

import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.mrezanasirloo.slick.middleware.BundleSlick;
import com.mrezanasirloo.slick.middleware.Middleware;
import com.mrezanasirloo.slick.middleware.Request;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-03-29
 */

public class MiddlewareValidatorFavoriteUpdateStream extends Middleware {


    @Inject
    public MiddlewareValidatorFavoriteUpdateStream() {
    }

    @Override
    public void handle(Request request, BundleSlick data) {
        RuntimeException error = null;
        if (data.isEmpty()) {
            error = new RuntimeException("Favorite called without parameter");
        }

        Integer id;
        if ((id = data.getParameter(null, Integer.class)) == null) {
            error = new RuntimeException("Favorite called with null parameter");
        }

        if (id != null && id == -1) {
            error = new RuntimeException("Favorite called with parameter that has a -1 ID");
        }
        if (error != null) {
            Logger.e(error, error.getMessage());
            ErrorHandler.handle(error);
            return;
        }
        request.next();
    }
}
