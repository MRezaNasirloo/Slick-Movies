package com.github.pedramrn.slick.parent.ui;

import android.support.v7.widget.Toolbar;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-18
 */

public interface ToolbarHost {
    ToolbarHost setToolbar(Toolbar toolbar);
    ToolbarHost setupButton(Toolbar toolbar, boolean enable);
}
