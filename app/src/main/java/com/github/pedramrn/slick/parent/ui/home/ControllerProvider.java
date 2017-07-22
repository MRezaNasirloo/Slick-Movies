package com.github.pedramrn.slick.parent.ui.home;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-16
 */
public interface ControllerProvider {
    Controller get(MovieBasic movie, String transitionName);
}
