package com.github.pedramrn.slick.parent.datasource.network.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-17
 */
public class InterceptorAuthToken implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.url().url().toString().contains("api.trakt.tv")) {
            request = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("trakt-api-version", "2")
                    .addHeader("trakt-api-key", "487a4a71669a58de841161c6130aa87ede2d7df01dd80573cfb53c87c20c3dde")
                    .build();
        } else if (request.url().url().toString().contains("api.themoviedb.org")){
            request = request.newBuilder()
                    .url(request.url().url().toString() + "&api_key=413d5af6c55f8b73b74d947fa6542ba1")
                    .build();
        }
        return chain.proceed(request);
    }
}
