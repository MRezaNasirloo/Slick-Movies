package com.github.pedramrn.slick.parent.utils.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.di.DaggerComponentApp;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleScheduler;
import com.github.pedramrn.slick.parent.ui.android.ImageLoader;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.github.pedramrn.slick.parent.ui.main.di.MainModule;

import org.mockito.Mockito;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public class TestApp extends App {

    @NonNull
    @Override
    protected DaggerComponentApp.Builder prepareDi() {
        return super.prepareDi().moduleNetwork(new ModuleNetwork() {
            @Override
            public String apiUrlTrakt() {
                return "https://api.test.com/";
            }

            @Override
            public String apiUrlTmdb() {
                return "https://api.test.com/";
            }
        }).moduleScheduler(new ModuleScheduler() {
            @Override
            public Scheduler provideBackgroundThreadScheduler() {
                return AndroidSchedulers.mainThread();
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


}
