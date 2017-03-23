package com.github.pedramrn.slick.parent.ui;

import android.app.Application;
import android.util.Log;

import com.bluelinelabs.conductor.Router;
//import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.github.pedramrn.slick.parent.BuildConfig;
import com.github.pedramrn.slick.parent.di.AppComponent;
import com.github.pedramrn.slick.parent.di.AppModule;
import com.github.pedramrn.slick.parent.di.DaggerAppComponent;
import com.github.pedramrn.slick.parent.ui.main.di.MainComponent;
import com.github.pedramrn.slick.parent.ui.main.di.MainModule;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-02
 */

public class App extends Application {

    private static AppComponent appComponent;
    private static MainComponent mainComponent;

    public static boolean loggedIn = false;
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        final long before = System.currentTimeMillis();
        LeakCanary.install(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
//        if (BuildConfig.DEBUG) {
//            AndroidDevMetrics.initWith(this);
//        }
        Log.e(TAG, "It took for application:" + (System.currentTimeMillis() - before));
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static MainComponent getMainComponent(Router router) {
        //        if (mainComponent == null) {
        mainComponent = appComponent.plus().mainModule(new MainModule(router)).build();
        //        }
        return mainComponent;
    }

    public static void disposeMainComponent() {
        mainComponent = null;
    }
}
