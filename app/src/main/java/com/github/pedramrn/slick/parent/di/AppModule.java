package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.BuildConfig;
import com.github.pedramrn.slick.parent.datasource.database.Models;
import com.github.pedramrn.slick.parent.datasource.database.UserEntity;
import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
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
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
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
    private final Context context;

    public AppModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    public Context getContext() {
        return context;
    }

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
    public ApiOmdb omdbClient(Retrofit.Builder builder) {
        return builder.baseUrl("http://www.omdbapi.com").build()
                .create(ApiOmdb.class);
    }

    @Provides
    public ApiTrakt boxOfficeWeekend(OkHttpClient okHttpClient) {
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
                .create(ApiTrakt.class);
    }


    @Provides
    public GsonConverterFactory gsonConverterFactory() {
        final Type type = new TypeToken<List<BoxOfficeItem>>() {
        }.getType();
        final Gson gson = new GsonBuilder().registerTypeAdapter(type, null).create();
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public ReactiveEntityStore<Persistable> entityStore(Context context) {
        final DatabaseSource source = new DatabaseSource(context, Models.DEFAULT, 1);
        if (BuildConfig.DEBUG) {
            // use this in development mode to drop and recreate the tables on every upgrade
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            source.setLoggingEnabled(true);
        }
        final Configuration configuration = source.getConfiguration();
        final ReactiveEntityStore<Persistable> entityStore =
                ReactiveSupport.toReactiveStore(new EntityDataStore<Persistable>(configuration));

        if (entityStore.select(UserEntity.class).get().firstOrNull() == null) {
            final UserEntity userEntity = new UserEntity();
            userEntity.setBio("You're awesome");
            userEntity.setAwesome(true);
            userEntity.setName("joe");
            entityStore.insert(userEntity).subscribe();
        }
        return entityStore;
    }

}
