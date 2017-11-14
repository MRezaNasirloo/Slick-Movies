package com.github.pedramrn.slick.parent.ui.details;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-14
 */

public abstract class ErrorHandlerSnackbar extends Snackbar.Callback implements View.OnClickListener {


    private final Snackbar snackbar;

    public ErrorHandlerSnackbar(@NonNull View view, String actionText) {
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                .addCallback(this)
                .setAction(actionText, this)
                .setDuration(60_000);
    }

    @Override
    public void onDismissed(Snackbar transientBottomBar, int event) {
        onDismissed(event);
    }

    @Override
    public void onClick(View v) {
        // onAction();
    }

    public void show(String message) {
        snackbar.setText(message).show();
    }

    public abstract void onDismissed(int event);


    // public abstract void onAction(); it can be done with just onDismissed callback
}
