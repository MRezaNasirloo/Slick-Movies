package com.github.pedramrn.slick.parent.ui.main.di;

import com.github.pedramrn.slick.parent.ui.boxoffice.ControllerBoxOffice;
import com.github.pedramrn.slick.parent.ui.main.ControllerMain;
import com.github.pedramrn.slick.parent.ui.ActivityMain;

import dagger.Subcomponent;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent {
    void inject(ActivityMain activity);

    void inject(ControllerMain homeController);

    void inject(ControllerBoxOffice controllerBoxOffice);

    @Subcomponent.Builder
    interface Builder {
        Builder mainModule(MainModule module);

        MainComponent build();

    }

}
