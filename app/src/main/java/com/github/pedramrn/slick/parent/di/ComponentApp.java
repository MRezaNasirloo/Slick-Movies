package com.github.pedramrn.slick.parent.di;

import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@Singleton
@Component(modules = {ModuleApp.class, ModuleNetwork.class, ModuleDatabase.class, ModuleScheduler.class})
public interface ComponentApp {
    ComponentMain.Builder plus();

}
