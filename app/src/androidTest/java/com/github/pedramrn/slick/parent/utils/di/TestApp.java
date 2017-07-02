package com.github.pedramrn.slick.parent.utils.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.di.DaggerComponentApp;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.ui.android.ImageLoader;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.github.pedramrn.slick.parent.ui.main.di.MainModule;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import org.mockito.Mockito;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public class TestApp extends App {

    private static HttpUrl httpUrl = HttpUrl.parse("/");
    private static OkHttp3IdlingResource okHttp3IdlingResource;
    private static OkHttpClient okHttpClient;

    public static void setBaseUrl(HttpUrl baseUrl) {
        TestApp.httpUrl = baseUrl;
    }

    @NonNull
    @Override
    protected DaggerComponentApp.Builder prepareDi() {
        return super.prepareDi().moduleNetwork(new ModuleNetwork() {
            @Override
            public HttpUrl apiUrlTrakt() {
                return TestApp.httpUrl;
            }

            @Override
            public HttpUrl apiUrlTmdb() {
                return TestApp.httpUrl;
            }

            @Override
            public OkHttpClient okHttpClient(List<Interceptor> interceptors) {
                return TestApp.okHttpClient;
            }
        });
    }

    @Override
    protected ComponentMain.Builder componentMainBuilder() {
        return super.componentMainBuilder().mainModule(new MainModule() {
            @Override
            public ImageLoader imageLoader() {
                ImageLoader imageLoader = Mockito.mock(ImageLoader.class);
                when(imageLoader.with(any(Context.class))).thenReturn(imageLoader);
                when(imageLoader.load(anyString())).thenReturn(imageLoader);
                return imageLoader;
            }
        });


    }

    public static OkHttp3IdlingResource getOkHttp3IdlingResource() {
        return okHttp3IdlingResource;
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        TestApp.okHttpClient = okHttpClient;
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
