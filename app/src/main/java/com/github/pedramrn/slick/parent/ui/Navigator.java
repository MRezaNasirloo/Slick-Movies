package com.github.pedramrn.slick.parent.ui;

import android.view.View;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public interface Navigator {
    Router getRouter();
    void navigateTo(Controller controller);

    View getView();
}
