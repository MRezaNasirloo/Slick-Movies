package com.github.pedramrn.slick.parent.ui.error;

import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.pedramrn.slick.parent.BuildConfig;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.annotations.NonNull;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-16
 */
public class ErrorHandler {

    private ErrorHandler() {
        //no instance
    }

    public static void handle(@Nullable Throwable error, @NonNull ErrorMessageHandler handler) {
        if (error == null) return;
        if (error instanceof UnknownHostException || error instanceof SocketTimeoutException || error instanceof ConnectException) {
            // TODO: 2017-11-13 extract String? how
            handler.error("Network Error, Are you Connected?");
            Crashlytics.log(Log.INFO, error.getClass().getSimpleName(), error.getMessage());
        } else {
            handler.error("Internal Error");
            if (BuildConfig.DEBUG) error.printStackTrace();
            Crashlytics.logException(error);
        }
    }
}
