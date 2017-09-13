package com.github.pedramrn.slick.parent.ui.list;

import com.bluelinelabs.conductor.Controller;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-05
 */

public interface OnItemAction {
    void action(Controller controller, int position);
}
