package com.github.pedramrn.slick.parent.ui.main;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 *         Created on: 2018-06-21
 */
public interface ViewIran {
    void showWarningIran();
    @NonNull
    Observable<Object> errorDismissed();
}
