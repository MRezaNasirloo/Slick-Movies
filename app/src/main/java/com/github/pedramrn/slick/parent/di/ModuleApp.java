package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@Singleton
@Module(subcomponents = ComponentMain.class)
public class ModuleApp {
    private static final String TAG = ModuleApp.class.getSimpleName();
    private final Context context;

    public ModuleApp(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    public Context getContext() {
        return context;
    }

    /**/

}
