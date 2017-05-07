package com.github.pedramrn.slick.parent.di;

import android.content.Context;
import android.content.res.AssetManager;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.MovieOmdb;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public class ApiMocker {

    private static List<BoxOfficeItem> boxOfficeItems;
    private static MovieOmdb movieOmdb;
    private static ApiMocker apiMocker;

    @SuppressWarnings("ConstantConditions")
    private ApiMocker(Context context) {
        AssetManager assets = context.getAssets();
        Type type = new TypeToken<List<BoxOfficeItem>>() {
        }.getType();
        InputStream inputStreamTrakt = null;
        InputStream inputStreamOmdb = null;
        try {
            inputStreamTrakt = assets.open("api_trakt.json");
            inputStreamOmdb = assets.open("api_omdb.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();
        boxOfficeItems = gson.fromJson(new JsonReader(new InputStreamReader(inputStreamTrakt)), type);
        movieOmdb = gson.fromJson(new JsonReader(new InputStreamReader(inputStreamOmdb)), MovieOmdb.class);
    }

    public static ApiMocker apiMocker(Context context) {
        if (apiMocker == null) {
            apiMocker = new ApiMocker(context);
        }
        return apiMocker;

    }

    public ApiOmdb mockApiOmdb() {
        ApiOmdb apiOmdb = Mockito.mock(ApiOmdb.class);
        //noinspection unchecked
        Mockito.when(apiOmdb.get(Mockito.anyString()))
                .thenReturn(movieOmdb(), movieOmdb(), movieOmdb(), movieOmdb(), movieOmdb(), movieOmdb(),
                        movieOmdb(), movieOmdb(), movieOmdb(), movieOmdb());
        return apiOmdb;

    }

    public ApiTrakt mockApiTrakt() {
        ApiTrakt apiTrakt = Mockito.mock(ApiTrakt.class);
        Mockito.when(apiTrakt.get()).thenReturn(Observable.just(boxOfficeItems));
        return apiTrakt;

    }

    public Observable<MovieOmdb> movieOmdb() {
        return Observable.just(getMovieOmdb());
    }

    public List<BoxOfficeItem> getBoxOfficeItems() {
        return boxOfficeItems;
    }

    public MovieOmdb getMovieOmdb() {
        return movieOmdb.toBuilder().imdbID(UUID.randomUUID().toString()).build();
    }
}
