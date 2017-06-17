package com.github.pedramrn.slick.parent;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.github.pedramrn.slick.parent.di.ComponentApp;
import com.github.pedramrn.slick.parent.di.DaggerComponentApp;
import com.github.pedramrn.slick.parent.di.ModuleApp;
import com.github.pedramrn.slick.parent.di.ModuleDatabase;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleNetworkMock;
import com.github.pedramrn.slick.parent.di.ModuleScheduler;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.github.pedramrn.slick.parent.ui.main.di.MainModule;
import com.squareup.leakcanary.LeakCanary;

//import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-02
 */

public class App extends Application {

    private static App app;
    protected ComponentApp componentApp;
    private ComponentMain componentMain;

    public static boolean loggedIn = false;
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        app = ((App) getApplicationContext());
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //        if (!BlockCanaryEx.isInSamplerProcess(this)) {
        //            BlockCanaryEx.install(new Config(this));
        //        }
        final long before = System.currentTimeMillis();
        //        LeakCanary.install(this);
        componentApp = prepareDi()
                .build();
        if (BuildConfig.DEBUG) {
            AndroidDevMetrics.initWith(this);
        }
        Log.e(TAG, "It took for application:" + (System.currentTimeMillis() - before));

        StrictMode.enableDefaults();
    }

    @NonNull
    protected DaggerComponentApp.Builder prepareDi() {
        return DaggerComponentApp.builder()
                .moduleApp(new ModuleApp(this))
                .moduleDatabase(new ModuleDatabase())
                .moduleNetwork(new ModuleNetwork())
                .moduleScheduler(new ModuleScheduler());
    }

    public static ComponentMain componentMain() {
        if (app.componentMain == null) {
            app.componentMain = app.componentMainBuilder().build();
        }
        return app.componentMain;
    }

    protected ComponentMain.Builder componentMainBuilder() {
        return componentApp.plus().mainModule(new MainModule());
    }

    public static void disposeComponentMain() {
        app.componentMain = null;
    }
}
