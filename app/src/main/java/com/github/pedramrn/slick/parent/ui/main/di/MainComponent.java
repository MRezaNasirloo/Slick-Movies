package com.github.pedramrn.slick.parent.ui.main.di;

import com.github.pedramrn.slick.parent.ui.boxoffice.ControllerBoxOffice;
import com.github.pedramrn.slick.parent.ui.main.ControllerMain;
import com.github.pedramrn.slick.parent.ui.ActivityMain;
import com.github.pedramrn.slick.parent.ui.popular.ControllerPopular;
import com.github.pedramrn.slick.parent.ui.upcoming.ControllerUpComing;

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

    void inject(ControllerUpComing controllerUpComing);

    void inject(ControllerPopular controllerPopular);

    @Subcomponent.Builder
    interface Builder {
        Builder mainModule(MainModule module);

        MainComponent build();

    }

}
