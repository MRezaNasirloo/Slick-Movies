package com.github.pedramrn.slick.parent;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.github.pedramrn.slick.parent.di.ComponentApp;
import com.github.pedramrn.slick.parent.di.DaggerComponentApp;
import com.github.pedramrn.slick.parent.di.ModuleApp;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleScheduler;
import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.github.pedramrn.slick.parent.ui.main.di.MainModule;
import com.mrezanasirloo.slick.middleware.RequestStack;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tspoon.traceur.Traceur;

import java.io.IOException;
import java.net.SocketException;

import io.fabric.sdk.android.Fabric;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

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
        RequestStack.getInstance().init(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        // IMMLeaks.fixFocusedViewLeak(this);
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

        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException) || (e instanceof SocketException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            Log.w(TAG, "Undeliverable exception received, not sure what to do", e);
        });

        refWatcher = RefWatcher.DISABLED;

        if (BuildConfig.BUILD_TYPE_DEBUG) {
            // Traceur.enableLogging();
            refWatcher = LeakCanary.install(this);
            // AndroidDevMetrics.initWith(this);
            // StrictMode.enableDefaults();
        }

        Crashlytics crashlytics = new Crashlytics.Builder()
                // disable crash reporting in debug build types with custom build type variable
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.BUILD_TYPE_DEBUG).build())
                .build();

        final Fabric fabric = new Fabric.Builder(this)
                .kits(crashlytics)
                //enable debugging with debuggable flag in build type
                .debuggable(BuildConfig.DEBUG)
                .build();

        Fabric.with(fabric);

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
                // .moduleDatabase(new ModuleDatabase())
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
        if (componentApp == null) {
            componentApp = prepareDi().build();
        }
        return componentApp.plus().mainModule(new MainModule());
    }

    public static void disposeComponentMain() {
        app.componentMain = null;
    }

    public static void disposeComponentApp() {
        app.componentApp = null;
    }
}
