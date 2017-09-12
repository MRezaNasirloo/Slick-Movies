package com.github.pedramrn.slick.parent;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pedramrn.slick.parent.di.ComponentApp;
import com.github.pedramrn.slick.parent.di.DaggerComponentApp;
import com.github.pedramrn.slick.parent.di.ModuleApp;
import com.github.pedramrn.slick.parent.di.ModuleDatabase;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleScheduler;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.github.pedramrn.slick.parent.ui.main.di.MainModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-02
 */

public class App extends Application {

    private static App app;
    protected ComponentApp componentApp;
    private ComponentMain componentMain;

    private static final String TAG = App.class.getSimpleName();
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        final long before = System.currentTimeMillis();
        super.onCreate();
        app = ((App) getApplicationContext());
        componentApp = prepareDi().build();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        /*if (!BlockCanaryEx.isInSamplerProcess(this)) {
            BlockCanaryEx.install(new Config(this));
        }*/

        refWatcher = RefWatcher.DISABLED;

        if (BuildConfig.DEBUG) {
            refWatcher = LeakCanary.install(this);
            // AndroidDevMetrics.initWith(this);
            StrictMode.enableDefaults();
        }
        Log.e(TAG, "It took for application:" + (System.currentTimeMillis() - before));
    }

    public static RefWatcher refWatcher(Context context) {
        if (context == null) {
            Log.wtf(TAG, "refWatcher called with null context");
            return RefWatcher.DISABLED;
        }
        return ((App) context.getApplicationContext()).refWatcher;
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
        if (componentApp == null) { componentApp = prepareDi().build(); }
        return componentApp.plus().mainModule(new MainModule());
    }

    public static void disposeComponentMain() {
        app.componentMain = null;
    }

    public static void disposeComponentApp() {
        app.componentApp = null;
    }
}
