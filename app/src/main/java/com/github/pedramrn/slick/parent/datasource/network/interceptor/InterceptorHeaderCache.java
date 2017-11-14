package com.github.pedramrn.slick.parent.datasource.network.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

import static com.github.pedramrn.slick.parent.util.Utils.isNetworkAvailable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-17
 */
public class InterceptorHeaderCache implements Interceptor {
    private final ConnectivityManager cm;


    public InterceptorHeaderCache(Context context) {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (isNetworkAvailable(cm)) {
            int maxAge = 60 * 60; // read from cache for 1 minute
            return originalResponse.newBuilder()
                    // .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
