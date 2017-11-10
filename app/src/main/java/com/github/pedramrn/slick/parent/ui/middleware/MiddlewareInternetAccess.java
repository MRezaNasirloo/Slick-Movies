package com.github.pedramrn.slick.parent.ui.middleware;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.github.slick.middleware.BundleSlick;
import com.github.slick.middleware.Middleware;
import com.github.slick.middleware.Request;

import javax.inject.Inject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-29
 */

public class MiddlewareInternetAccess extends Middleware {


    private final Context context;

    @Inject
    public MiddlewareInternetAccess(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void handle(Request request, BundleSlick date) {
        if (isNetworkAvailable(context)) {
            request.next();// process the next request
        } else {
            //            request.stopped();// optional, let the callback know about it
            // Navigator.go(ActivityError.class);
            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
