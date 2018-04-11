package com.github.pedramrn.slick.parent.ui.middleware;

import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
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

public class MiddlewareValidatorFavorite extends Middleware {


    @Inject
    public MiddlewareValidatorFavorite() {
    }

    @Override
    public void handle(Request request, BundleSlick data) {
        RuntimeException error = null;
        if (data.isEmpty()) {
            error = new RuntimeException("Favorite called without parameter");
        }

        FavoriteDomain fd;
        if ((fd = data.getParameter(null, FavoriteDomain.class)) == null) {
            error = new RuntimeException("Favorite called with null parameter");
        }

        if (fd != null && fd.tmdb() == -1) {
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
