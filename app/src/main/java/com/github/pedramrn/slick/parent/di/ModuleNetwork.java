package com.github.pedramrn.slick.parent.di;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */
@Singleton
@Module(subcomponents = ComponentMain.class)
public class ModuleNetwork {

    @Provides
    public OkHttpClient okHttpClient() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    public Retrofit.Builder retrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                //                .baseUrl("https://api.themoviedb.org/3/")
                //                .baseUrl("http://www.omdbapi.com")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                //                .addConverterFactory()
                ;
    }

    @Provides
    public ApiOmdb omdbClient(Retrofit.Builder builder) {
        return builder.baseUrl("http://www.omdbapi.com").build()
                .create(ApiOmdb.class);
    }

    @Provides
    public ApiTmdb tmdbClient(@Named("tmdb") String url, OkHttpClient okHttpClient, Gson gson) {
        final OkHttpClient httpClient = okHttpClient.newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request request = chain.request()
                        .newBuilder()
                        .url(chain.request().url().url().toString() + "&api_key=413d5af6c55f8b73b74d947fa6542ba1")
                        .build();
                return chain.proceed(request);
            }
        }).build();
        return new Retrofit.Builder()
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(url).build()
                .create(ApiTmdb.class);
    }

    @Provides
    public ApiTrakt boxOfficeWeekend(@Named("trakt") String url, OkHttpClient okHttpClient, Gson gson) {
        final OkHttpClient httpClient = okHttpClient.newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("trakt-api-version", "2")
                        .addHeader("trakt-api-key", "487a4a71669a58de841161c6130aa87ede2d7df01dd80573cfb53c87c20c3dde")
                        .build();
                return chain.proceed(request);
            }
        }).build();
        return new Retrofit.Builder()
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(url).build()
                .create(ApiTrakt.class);
    }

    @Provides
    public Gson gsonConverterFactory() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();
        /*final Type type = new TypeToken<List<BoxOfficeItem>>() {
        }.getType();
        final Gson gson = new GsonBuilder().registerTypeAdapter(type, null).componentMainBuilder();
        return GsonConverterFactory.componentMainBuilder(gson);*/
    }

    @Provides
    @Named("trakt")
    public String apiUrlTrakt() {
        return "https://api.trakt.tv";
    }


    @Provides
    @Named("tmdb")
    public String apiUrlTmdb() {
        return "https://api.themoviedb.org/3/";
    }

}
