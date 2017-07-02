package com.github.pedramrn.slick.parent.di;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */
@Singleton
@Module(subcomponents = ComponentMain.class)
public class ModuleNetwork extends ModuleNetworkBase {

    @Provides
    @Singleton
    public OkHttpClient okHttpClient(List<Interceptor> interceptors) {
        return super.baseOkHttpClient(interceptors);
    }

    @Provides
    @Singleton
    public List<Interceptor> interceptors() {
        return super.baseInterceptors();
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
