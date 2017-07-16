package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */
public class ModuleNetworkBase {

    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (request.url().url().toString().contains("api.trakt.tv")) {
                request = request.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("trakt-api-version", "2")
                        .addHeader("trakt-api-key", "487a4a71669a58de841161c6130aa87ede2d7df01dd80573cfb53c87c20c3dde")
                        .build();
            } else {
                request = request.newBuilder()
                        .url(request.url().url().toString() + "&api_key=413d5af6c55f8b73b74d947fa6542ba1")
                        .build();
            }
            return chain.proceed(request);
        }
    };

    public OkHttpClient baseOkHttpClient(List<Interceptor> interceptors, List<Interceptor> interceptorsNetwork, Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        for (Interceptor interceptor : interceptorsNetwork) {
            builder.addNetworkInterceptor(interceptor);
        }
        return builder.build();
    }

    public Cache baseCache(Context context) {
        return new Cache(context.getCacheDir(), 30 * 1024 * 1024);//30MB
    }

    public List<Interceptor> baseInterceptors() {
        return new ArrayList<>(Collections.singletonList(interceptor));
    }

    public Retrofit.Builder baseRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                ;
    }

    public ApiOmdb baseOmdbClient(Retrofit.Builder builder, OkHttpClient client) {
        return builder.client(client)
                .baseUrl("http://www.omdbapi.com")
                .build()
                .create(ApiOmdb.class);
    }

    public ApiTmdb baseApiTmdb(@Named("tmdb") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson) {
        return builder
                .client(okHttpClient)
                .baseUrl(url).build()
                .create(ApiTmdb.class);
    }

    public ApiTrakt baseApiTrakt(@Named("trakt") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson) {
        return builder
                .client(okHttpClient)
                .baseUrl(url)
                .build()
                .create(ApiTrakt.class);
    }

    public Gson baseGsonConverterFactory() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();
    }

    public HttpUrl baseApiUrlTrakt() {
        return HttpUrl.parse("https://api.trakt.tv");
    }

    public HttpUrl baseApiUrlTmdb() {
        return HttpUrl.parse("https://api.themoviedb.org/3/");
    }

}
