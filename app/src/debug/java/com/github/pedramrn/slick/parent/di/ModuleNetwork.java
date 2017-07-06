package com.github.pedramrn.slick.parent.di;

import android.content.Context;
import android.content.res.AssetManager;

import com.github.pedramrn.slick.parent.ApiTmdbMock;
import com.github.pedramrn.slick.parent.ApiTraktMock;
import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.github.pedramrn.slick.parent.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */
@Singleton
@Module(subcomponents = ComponentMain.class)
public class ModuleNetwork extends ModuleNetworkBase {

    private static boolean MOCK_MODE = false;

    public ModuleNetwork() {
        // TODO: 2017-07-02 read from preference manager
        // MOCK_MODE = true;
    }


    @Provides
    @Singleton
    public OkHttpClient okHttpClient(List<Interceptor> interceptors) {
        return super.baseOkHttpClient(interceptors);
    }

    @Provides
    @Singleton
    public List<Interceptor> interceptors() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ArrayList<Interceptor> interceptors = new ArrayList<>(1);
        interceptors.add(interceptor);
        return interceptors;
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
    public ApiTmdb apiTmdb(@Named("tmdb") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson,
                           NetworkBehavior behavior, List<MovieTmdb> tmdbList) {
        if (MOCK_MODE) {
            return new ApiTmdbMock(behavior, gson, tmdbList);
        }
        return super.baseApiTmdb(url, okHttpClient, builder, gson);

    }

    @Provides
    @Singleton
    public ApiTrakt apiTrakt(@Named("trakt") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson, NetworkBehavior behavior,
                             List<MovieTraktMetadata> popular, List<MovieTraktPageMetadata> trending) {
        if (MOCK_MODE) {
            return new ApiTraktMock(behavior, gson, trending, popular);
        }
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

    @Provides
    @Singleton
    public NetworkBehavior networkBehavior() {
        NetworkBehavior networkBehavior = NetworkBehavior.create();
        networkBehavior.setDelay(2, TimeUnit.SECONDS);
        networkBehavior.setVariancePercent(40);
        networkBehavior.setFailurePercent(0);
        networkBehavior.setErrorPercent(0);
        return networkBehavior;
    }

    /*Mock Data*/

    @Provides
    @Singleton
    public AssetManager asset(Context context) {
        return context.getAssets();
    }

    @Provides
    @Singleton
    public List<MovieTraktMetadata> popular(AssetManager asset, Gson gson) {
        InputStream stream;
        String json = "[]";
        try {
            stream = asset.open("api_trakt_popular_200.json");
            json = FileUtils.readFile(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<List<MovieTraktMetadata>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Provides
    @Singleton
    public List<MovieTraktPageMetadata> trending(AssetManager asset, Gson gson) {
        InputStream stream;
        String json = "[]";
        try {
            stream = asset.open("api_trakt_trending_200.json");
            json = FileUtils.readFile(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<List<MovieTraktPageMetadata>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Provides
    @Singleton
    public List<MovieTmdb> tmdbList(AssetManager asset, Gson gson) {
        InputStream stream;
        String json = "[]";
        try {
            stream = asset.open("api_tmdb_movie_list.json");
            json = FileUtils.readFile(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<List<MovieTmdb>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}
