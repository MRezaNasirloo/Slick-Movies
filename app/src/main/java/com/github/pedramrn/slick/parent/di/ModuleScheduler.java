package com.github.pedramrn.slick.parent.di;

import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

@Module(subcomponents = ComponentMain.class)
public class ModuleScheduler {

    @Provides
    @Named("main")
    public Scheduler provideMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named("io")
    public Scheduler provideBackgroundThreadScheduler() {
        return Schedulers.io();
    }
}
