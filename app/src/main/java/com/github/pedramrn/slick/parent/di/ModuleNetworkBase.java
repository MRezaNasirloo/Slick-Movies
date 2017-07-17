package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.datasource.network.interceptor.InterceptorAuthToken;
import com.github.pedramrn.slick.parent.datasource.network.interceptor.InterceptorHeaderCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */
public class ModuleNetworkBase {

    public OkHttpClient baseOkHttpClient(List<Interceptor> interceptors, List<Interceptor> interceptorsNetwork, Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);
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

    /**
     * @param context app context
     * @return a singleton list, create a new list if you want a fully working list
     */
    public List<Interceptor> baseInterceptors(Context context) {
        return Collections.singletonList((Interceptor) new InterceptorAuthToken());
    }

    /**
     * @param context app context
     * @return a singleton list, create a new list if you want a fully working list
     */
    public List<Interceptor> baseNetworkInterceptors(Context context) {
        return Collections.singletonList((Interceptor) new InterceptorHeaderCache(context));
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
