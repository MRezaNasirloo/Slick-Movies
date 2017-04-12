package com.github.pedramrn.slick.parent.di;

import com.github.pedramrn.slick.parent.datasource.network.OmdbApi;
import com.github.pedramrn.slick.parent.datasource.network.TraktApi;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.ui.main.di.MainComponent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
 *         Created on: 2017-03-01
 */
@Module(subcomponents = MainComponent.class)
public class AppModule {
    private static final String TAG = AppModule.class.getSimpleName();

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
    public Retrofit.Builder retrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                //                .baseUrl("https://api.themoviedb.org/3/")
                //                .baseUrl("http://www.omdbapi.com")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    @Provides
    public OmdbApi omdbClient(Retrofit.Builder builder) {
        return builder.baseUrl("http://www.omdbapi.com").build()
                .create(OmdbApi.class);
    }

    @Provides
    public TraktApi boxOfficeWeekend(OkHttpClient okHttpClient) {
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
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.trakt.tv").build()
                .create(TraktApi.class);
    }


    @Provides
    public GsonConverterFactory gsonConverterFactory() {
        final Type type = new TypeToken<List<BoxOfficeItem>>() {
        }.getType();
        final Gson gson = new GsonBuilder().registerTypeAdapter(type, null).create();
        return GsonConverterFactory.create(gson);
    }

}
