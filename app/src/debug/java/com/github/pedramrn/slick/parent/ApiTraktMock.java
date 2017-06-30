package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.AnticipatedTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.TraktPageMetadata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */

public class ApiTraktMock implements ApiTrakt {

    private final Type type = new TypeToken<List<TraktPageMetadata>>() {
    }.getType();
    private final BehaviorDelegate<ApiTrakt> delegate;
    private final String trendingJson;
    private final Gson gson;
    private List<TraktPageMetadata> trendingList;

    public ApiTraktMock(BehaviorDelegate<ApiTrakt> delegate, String trendingJson, Gson gson) {
        this.delegate = delegate;
        this.trendingJson = trendingJson;
        this.gson = gson;
        init(trendingJson, gson);
    }

    public ApiTraktMock(String trendingJson, Gson gson) {
        this.trendingJson = trendingJson;
        this.gson = gson;
        delegate = createBehavior(gson);
        init(trendingJson, gson);
    }

    private void init(String trendingJson, Gson gson) {
        trendingList = gson.fromJson(trendingJson, type);
    }

    public static BehaviorDelegate<ApiTrakt> createBehavior(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://api.trakt.tv").build();
        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit).networkBehavior(NetworkBehavior.create()).build();
        return mockRetrofit.create(ApiTrakt.class);
    }

    @Override
    public Observable<List<BoxOfficeItem>> get() {
        return delegate.returningResponse(null).get();
    }

    @Override
    public Observable<AnticipatedTrakt> anticipated() {
        return delegate.returningResponse(null).anticipated();
    }

    @Override
    public Observable<List<TraktPageMetadata>> anticipatedMetadata() {
        return null;
    }

    @Override
    public Observable<List<TraktPageMetadata>> anticipatedMetadata(@Query("page") int page, @Query("limit") int size) {
        return null;
    }

    @Override
    public Observable<List<TraktPageMetadata>> trending(@Query("page") int page, @Query("limit") int size) {
        List<TraktPageMetadata> response = Observable.fromIterable(trendingList)
                .skip((page - 1) * size)
                .take(size)
                .toList()
                .blockingGet();
        return delegate.returningResponse(response).trending(page, size);
    }
}
