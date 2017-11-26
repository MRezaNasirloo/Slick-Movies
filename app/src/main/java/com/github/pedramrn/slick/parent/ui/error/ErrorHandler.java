package com.github.pedramrn.slick.parent.ui.error;

import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.pedramrn.slick.parent.BuildConfig;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-16
 */
public class ErrorHandler {

    private static boolean enable;

    private ErrorHandler() {
        //no instance
    }

    public static void enable() {
        enable = true;
    }

    public static void disable() {
        enable = false;
    }

    public static String handle(@Nullable Throwable error) {
        if (error == null) return null;
        if (error instanceof UnknownHostException || error instanceof SocketTimeoutException || error instanceof ConnectException) {
            // TODO: 2017-11-13 extract String? how
            if (enable) Crashlytics.log(Log.INFO, error.getClass().getSimpleName(), error.getMessage());
            return "Network Error, Are you Connected?";
        } else {
            if (BuildConfig.DEBUG) error.printStackTrace();
            if (enable) Crashlytics.logException(error);
            return "Internal Error";
        }
    }
}