package com.github.pedramrn.slick.parent.ui;

import android.app.Application;

import com.bluelinelabs.conductor.Router;
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

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
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
