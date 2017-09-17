package com.github.pedramrn.slick.parent.ui.details;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-18
 */

public abstract class ControllerBase extends Controller implements ToolbarHost, Navigator {
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
        //emulates no-op impl
        return this;
    }


    @Override
    public ToolbarHost setupButton(Toolbar toolbar, boolean enable) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof ToolbarHost) {
            return ((ToolbarHost) activity).setupButton(toolbar, enable);
        }//emulates no-op impl
        return this;
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        setToolbar(null);
        super.onDestroyView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.refWatcher(getActivity()).watch(this);
    }

    protected void renderError(@Nullable Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
            Toast.makeText(ControllerBase.this.getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void navigateTo(Controller controller) {
        getRouter().pushController(RouterTransaction.with(controller)
                                           .popChangeHandler(new HorizontalChangeHandler())
                                           .pushChangeHandler(new HorizontalChangeHandler()));
    }
}
