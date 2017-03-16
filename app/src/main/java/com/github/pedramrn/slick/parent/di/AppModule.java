package com.github.pedramrn.slick.parent.di;

import android.util.Log;

import com.github.pedramrn.slick.parent.datasource.database.VoteRepository;
import com.github.pedramrn.slick.parent.datasource.network.BoxOfficeWeekend;
import com.github.pedramrn.slick.parent.datasource.network.OmdbClient;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.ui.main.di.MainComponent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
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

    public OmdbClient omdbClient(Retrofit.Builder builder) {
        return builder.baseUrl("").build()
                .create(OmdbClient.class);
    }

    @Provides
    public BoxOfficeWeekend boxOfficeWeekend(Retrofit.Builder builder) {
        return builder.baseUrl("https://api.trakt.tv").build()
                .create(BoxOfficeWeekend.class);
    }

    @Provides
    public VoteRepository voteRepository() {
        return new VoteRepository() {
            @Override
            public String voteUp(String id) {
                Log.d(TAG, "execute() called after middleware");
                return "HI";
            }

            @Override
            public Observable<String> voteDown(String id) {
                Log.d(TAG, "voteDown() called after middleware");
                return Observable.just("Hi");
            }

            @Override
            public Completable deleteVote(String id) {
                return Completable.complete();
            }
        };
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory() {
        final Type type = new TypeToken<List<BoxOfficeItem>>() {
        }.getType();
        final Gson gson = new GsonBuilder().registerTypeAdapter(type, null).create();
        return GsonConverterFactory.create(gson);
    }

}
