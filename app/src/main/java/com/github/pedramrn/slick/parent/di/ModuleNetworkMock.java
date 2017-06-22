package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.MovieOmdb;
import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.http.Query;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-05-07
 */

public class ModuleNetworkMock extends ModuleNetwork {

    /*private final Context context;

    public ModuleNetworkMock(Context context) {
        this.context = context;
    }

    @Override
    public ApiOmdb omdbClient(Retrofit.Builder builder) {
        return new ApiOmdb() {
            @Override
            public Observable<MovieOmdb> get(@Query("i") String id) {
                return Observable.just(ApiMocker.apiMocker(context).getMovieOmdb());
            }
        };
    }

    @Override
    public ApiTrakt boxOfficeWeekend(OkHttpClient okHttpClient, Gson gson) {
        return new ApiTrakt() {
            @Override
            public Observable<List<BoxOfficeItem>> get() {
                return Observable.just(ApiMocker.apiMocker(context).getBoxOfficeItems());
            }
        };
    }*/
}
