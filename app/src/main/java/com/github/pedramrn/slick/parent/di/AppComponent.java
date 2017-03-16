package com.github.pedramrn.slick.parent.di;

import com.github.pedramrn.slick.parent.ui.main.di.MainComponent;

import dagger.Component;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@Component(modules = AppModule.class)
public interface AppComponent {
    MainComponent.Builder plus();
}
