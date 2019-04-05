package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-03-01
 */
@Module(subcomponents = ComponentMain.class)
public class ModuleApp {
    private static final String TAG = ModuleApp.class.getSimpleName();
    private final Context context;

    public ModuleApp(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    public Context getContext() {
        return context;
    }

    @Provides
    @Singleton
    public DatabaseReference reference() {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.setPersistenceEnabled(true);
        instance.setLogLevel(Logger.Level.INFO);
        return instance.getReference();
    }

    /**/

}
