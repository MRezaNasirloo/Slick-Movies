package com.github.pedramrn.slick.parent.ui.main.di;

import com.github.pedramrn.slick.parent.ui.main.HomeController;
import com.github.pedramrn.slick.parent.ui.MainActivity;

import dagger.Subcomponent;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);

    void inject(HomeController homeController);

    @Subcomponent.Builder
    interface Builder {
        Builder mainModule(MainModule module);

        MainComponent build();
    }
}
