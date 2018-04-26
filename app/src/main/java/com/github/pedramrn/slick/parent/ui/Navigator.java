package com.github.pedramrn.slick.parent.ui;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-09-17
 */
public interface Navigator {
    void navigateTo(@NonNull Screen screen);

    void navigateTo(@NonNull Screen screen, View sharedView, String transitionName);

    @NonNull
    SnackbarManager snackbarManager();
}
