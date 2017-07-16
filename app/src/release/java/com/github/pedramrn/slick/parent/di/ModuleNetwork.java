package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static com.github.pedramrn.slick.parent.util.Utils.isNetworkAvailable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */
@Singleton
@Module(subcomponents = ComponentMain.class)
public class ModuleNetwork extends ModuleNetworkBase {

    @Provides
    @Singleton
    public OkHttpClient okHttpClient(@Named("interceptors") List<Interceptor> interceptors,
                                     @Named("interceptors_network") List<Interceptor> interceptorsNetwork,
                                     Cache cache) {
        return super.baseOkHttpClient(interceptors, interceptorsNetwork, cache);
    }

    @Provides
    @Singleton
    public Cache cache(Context context) {
        return super.baseCache(context);
    }

    @Provides
    @Singleton
    @Named("interceptors")
    public List<Interceptor> interceptors() {
        List<Interceptor> list = super.baseInterceptors();
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        list.add(interceptor);
        return list;
    }

    @Provides
    @Singleton
    @Named("interceptors_network") // FIXME: 2017-07-16 dirty
    public List<Interceptor> networkInterceptors(final Context context) {
        final List<Interceptor> list = new ArrayList<>(1);
        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (isNetworkAvailable(context)) {
                    int maxAge = 60 * 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
        list.add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        return list;
    }

    @Provides
    @Singleton
    public Retrofit.Builder retrofit(Gson gson) {
        return super.baseRetrofit(gson);
    }

    @Provides
    @Singleton
    public ApiOmdb omdbClient(Retrofit.Builder builder, OkHttpClient client) {
        return super.baseOmdbClient(builder, client);
    }

    @Provides
    @Singleton
    public ApiTmdb apiTmdb(@Named("tmdb") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson) {
        return super.baseApiTmdb(url, okHttpClient, builder, gson);

    }

    @Provides
    @Singleton
    public ApiTrakt apiTrakt(@Named("trakt") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson) {
        return super.baseApiTrakt(url, okHttpClient, builder, gson);
    }

    @Provides
    @Singleton
    public Gson gsonConverterFactory() {
        return super.baseGsonConverterFactory();
    }

    @Provides
    @Singleton
    @Named("trakt")
    public HttpUrl apiUrlTrakt() {
        return super.baseApiUrlTrakt();
    }

    @Provides
    @Singleton
    @Named("tmdb")
    public HttpUrl apiUrlTmdb() {
        return super.baseApiUrlTmdb();
    }

}
