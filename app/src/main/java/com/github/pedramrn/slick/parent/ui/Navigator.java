package com.github.pedramrn.slick.parent.ui;

import android.support.annotation.NonNull;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-09-17
 */
public interface Navigator {
    void navigateTo(@NonNull Screen screen);

    @NonNull
    SnackbarManager snackbarManager();
}
