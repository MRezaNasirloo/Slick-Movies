package com.github.pedramrn.slick.parent.ui.main;

import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.View;

import javax.inject.Inject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public class SnackbarLogin {

    View view;

    @Inject
    public SnackbarLogin(View view) {
        this.view = view;
    }

    public void show() {
        Snackbar.make(view, "Dude ya gotta login.", Snackbar.LENGTH_SHORT).show();
    }
}
