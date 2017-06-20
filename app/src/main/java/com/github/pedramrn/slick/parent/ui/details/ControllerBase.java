package com.github.pedramrn.slick.parent.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-18
 */

public abstract class ControllerBase extends Controller implements ToolbarHost {
    protected ControllerBase(@Nullable Bundle args) {
        super(args);
    }

    protected ControllerBase() {
    }


    @Override
    public ToolbarHost setToolbar(Toolbar toolbar) {
        if (getActivity() != null) {
            return ((ToolbarHost) getActivity()).setToolbar(toolbar);
        }
        //emulate no-op impl
        return this;
    }


    @Override
    public ToolbarHost setupButton(boolean enable) {
        if (getActivity() != null) {
            return ((ToolbarHost) getActivity()).setupButton(enable);
        }//emulate no-op impl
        return this;
    }

}
