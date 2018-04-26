package com.github.pedramrn.slick.parent.ui;

import android.transition.Transition;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 * Created on: 2018-04-26
 */
public interface ScreenTransition {
    Transition sharedElementEnterTransition();

    Transition sharedElementReturnTransition();

    Transition exitTransition();

    Transition enterTransition();

    Transition reenterTransition();
}
