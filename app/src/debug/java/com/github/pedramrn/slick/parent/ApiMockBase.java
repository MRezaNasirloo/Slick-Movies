package com.github.pedramrn.slick.parent;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */

public abstract class ApiMockBase<API> {
    protected final BehaviorDelegate<API> delegate;
    protected final Gson gson;

    public ApiMockBase(BehaviorDelegate<API> delegate, String jsonData, Gson gson) {
        this.delegate = delegate;
        this.gson = gson;
        init(jsonData, gson);

    }

    public ApiMockBase(String jsonData, Gson gson) {
        this.gson = gson;
        delegate = createBehavior(gson);
        init(jsonData, gson);
    }

    protected BehaviorDelegate<API> createBehavior(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(getBaseUrl()).build();
        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit).networkBehavior(NetworkBehavior.create()).build();
        return mockRetrofit.create(getApiClassType());
    }

    @NonNull
    protected String getBaseUrl() {
        return "https://example.com";
    }

    protected abstract Class<API> getApiClassType();

    protected abstract void init(String jsonData, Gson gson);
}
