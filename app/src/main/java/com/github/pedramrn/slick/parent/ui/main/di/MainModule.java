package com.github.pedramrn.slick.parent.ui.main.di;

import com.github.pedramrn.slick.parent.ui.android.ImageLoader;
import com.github.pedramrn.slick.parent.ui.android.ImageLoaderPicassoImpl;

import dagger.Module;
import dagger.Provides;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@Module
@ControllerScope
public class MainModule {

    @Provides
    public ImageLoader imageLoader() {
        return new ImageLoaderPicassoImpl();
    }

    /*private final Router router;

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
    }*/
}
