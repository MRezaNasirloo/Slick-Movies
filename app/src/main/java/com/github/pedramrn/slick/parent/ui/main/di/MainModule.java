package com.github.pedramrn.slick.parent.ui.main.di;

import com.bluelinelabs.conductor.Router;

import java.util.UUID;

import dagger.Module;
import dagger.Provides;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@Module
public class MainModule {

    private final Router router;

    public MainModule(Router router) {
        this.router = router;
    }

    @ActivityScope
    @Provides
    static String string() {
        return UUID.randomUUID().toString();
    }

    @Provides
    public Router getRouter() {
        return router;
    }
}
