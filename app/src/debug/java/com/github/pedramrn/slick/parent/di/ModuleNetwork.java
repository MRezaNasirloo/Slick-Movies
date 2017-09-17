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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
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
         MOCK_MODE = true;
    }


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

    @Override
    public List<Interceptor> baseInterceptors(Context context) {
        List<Interceptor> list = new ArrayList<>(super.baseInterceptors(context));
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        list.add(interceptor);
        return list;
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
    public ApiTmdb apiTmdb(@Named("tmdb") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson,
                           NetworkBehavior behavior, List<MovieTmdb> tmdbList, List<MovieTraktPageMetadata> traktList
    ) {
        if (MOCK_MODE) {
            return new ApiTmdbMock(behavior, gson, tmdbList, traktList);
        }
        return baseApiTmdb(url, okHttpClient, builder, gson);

    }

    @Provides
    @Singleton
    public ApiTrakt apiTrakt(@Named("trakt") HttpUrl url, OkHttpClient okHttpClient, Retrofit.Builder builder, Gson gson,
                             NetworkBehavior behavior, List<MovieTraktMetadata> popular, List<MovieTraktPageMetadata> trending) {
        if (MOCK_MODE) {
            return new ApiTraktMock(behavior, gson, trending, popular);
        }
        return baseApiTrakt(url, okHttpClient, builder, gson);
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
    public NetworkBehavior networkBehavior() {
        NetworkBehavior networkBehavior = NetworkBehavior.create();
        networkBehavior.setDelay(1, TimeUnit.SECONDS);
        networkBehavior.setVariancePercent(0);
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
        if (!MOCK_MODE) return Collections.emptyList();
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
        if (!MOCK_MODE) return Collections.emptyList();
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
        if (!MOCK_MODE) return Collections.emptyList();
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
