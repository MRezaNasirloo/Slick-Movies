package com.github.pedramrn.slick.parent.ui.details;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.Screen;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;

import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-18
 */

public abstract class ControllerBase extends Controller implements ToolbarHost, Navigator {

    protected final PublishSubject<Object> retry = PublishSubject.create();
    protected final PublishSubject<Object> errorDismissed = PublishSubject.create();
    private ErrorHandlerSnackbar snackbar;

    protected ControllerBase(@Nullable Bundle args) {
        super(args);
    }

    protected ControllerBase() {
    }

    private static final String TAG = ControllerBase.class.getSimpleName();


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
        snackbar = null;
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
    public void navigateTo(@NonNull Screen screen) {
        /*getRouter().pushController(RouterTransaction.with(screen)
                .popChangeHandler(new HorizontalChangeHandler())
                .pushChangeHandler(new HorizontalChangeHandler()));*/
    }

    @Override
    protected void onAttach(@NonNull View view) {
        snackbar = new ErrorHandlerSnackbar(view, getSnackbarActionText()) {
            @Override
            public void onDismissed(int event) {
                errorDismissed.onNext(1);
                if (Snackbar.Callback.DISMISS_EVENT_ACTION == event || Snackbar.Callback.DISMISS_EVENT_TIMEOUT == event) {
                    retry.onNext(1);
                }
            }
        };
    }

    @NonNull
    protected String getSnackbarActionText() {
        return "Retry";
    }

    protected void showSnakbar(@NonNull String message) {
        snackbar.show(message);
    }

    @Override
    public void navigateTo(@NonNull Screen screen, View sharedView, String transitionName) {

    }
}
