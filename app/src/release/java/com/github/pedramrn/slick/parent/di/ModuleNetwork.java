package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.ApiIpLocation;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */
@Module(subcomponents = ComponentMain.class)
public class ModuleNetwork extends ModuleNetworkBase {

    @Provides
    @Singleton
    public OkHttpClient okHttpClient(@Named("interceptors") List<Interceptor> interceptors,
                                     @Named("interceptors_network") List<Interceptor> interceptorsNetwork,
                                     Cache cache) {
        return baseOkHttpClient(interceptors, interceptorsNetwork, cache);
    }

    @Provides
    @Singleton
    public Cache cache(Context context) {
        return baseCache(context);
    }

    @Provides
    @Singleton
    @Named("interceptors")
    public List<Interceptor> interceptors(Context context) {
        return baseInterceptors(context);
    }

    @Provides
    @Singleton
    @Named("interceptors_network")
    public List<Interceptor> networkInterceptors(Context context) {
        return baseNetworkInterceptors(context);
    }

    @Provides
    @Singleton
    public Retrofit.Builder retrofit(Gson gson) {
        return baseRetrofit(gson);
    }

    @Provides
    @Singleton
    public ApiOmdb omdbClient(Retrofit.Builder builder, OkHttpClient client) {
        return baseOmdbClient(builder, client);
    }

    @Provides
    @Singleton
    public ApiTmdb apiTmdb(@Named("tmdb") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson) {
        return baseApiTmdb(url, okHttpClient, builder, gson);

    }

    @Provides
    @Singleton
    public ApiTrakt apiTrakt(@Named("trakt") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson) {
        return super.baseApiTrakt(url, okHttpClient, builder, gson);
    }

    @Provides
    @Singleton
    public ApiIpLocation apiIpLocation(@Named("ip-api") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson) {
        return baseApiIpApi(url, okHttpClient, builder, gson);
    }

    @Provides
    @Singleton
    public Gson gsonConverterFactory() {
        return baseGsonConverterFactory();
    }

    @Provides
    @Singleton
    @Named("trakt")
    public HttpUrl apiUrlTrakt() {
        return baseApiUrlTrakt();
    }

    @Provides
    @Singleton
    @Named("tmdb")
    public HttpUrl apiUrlTmdb() {
        return baseApiUrlTmdb();
    }

    @Provides
    @Singleton
    @Named("ip-api")
    public HttpUrl apiUrlIpApi() {
        return baseApiUrlIpApi();
    }

    @Provides
    @Singleton
    public RouterBoxOffice routerBoxOffice(ApiTmdb apiTmdb, ApiTrakt apiTrakt, MapperMovie mapper, @Named("io") Scheduler io) {
        return baseRouterBoxOffice(apiTmdb, apiTrakt, mapper, io);
    }
}
