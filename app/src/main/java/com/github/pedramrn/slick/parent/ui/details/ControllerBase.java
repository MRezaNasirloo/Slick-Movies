package com.github.pedramrn.slick.parent.ui.details;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
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
        Activity activity = getActivity();
        if (activity != null && activity instanceof ToolbarHost) {
            return ((ToolbarHost) activity).setToolbar(toolbar);
        }
        //emulate no-op impl
        return this;
    }


    @Override
    public ToolbarHost setupButton(boolean enable) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof ToolbarHost) {
            return ((ToolbarHost) activity).setupButton(enable);
        }//emulate no-op impl
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.refWatcher(getActivity()).watch(this);
    }
}
