package com.github.pedramrn.slick.parent.ui.error;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.pedramrn.slick.parent.BuildConfig;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.Constants;
import com.github.pedramrn.slick.parent.ui.details.router.InformationNotAvailableException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava2.HttpException;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-16
 */
public class ErrorHandler {

    private static boolean enable = true;

    private ErrorHandler() {
        //no instance
    }

    public static void enable() {
        enable = true;
    }

    public static void disable() {
        enable = false;
    }

    /**
     * @param error to handle
     * @return code
     */
    public static short handle(@Nullable Throwable error) {
        if (error == null) return -1;
        //noinspection deprecation
        if (error instanceof UnknownHostException
                || error instanceof SocketTimeoutException
                || error instanceof ConnectException
                || error instanceof HttpException// FIXME: 2018-03-09 retrofit throws this exception on 429 too many requests.
                ) {
            if (enable) Crashlytics.log(Log.INFO, error.getClass().getSimpleName(), error.getMessage());
            if (enable) Crashlytics.logException(error);
            return Constants.ERROR_CODE_NETWORK;
        } else if (error instanceof InformationNotAvailableException) {
            if (enable) Crashlytics.log(Log.INFO, error.getClass().getSimpleName(), error.getMessage());
            if (enable) Crashlytics.logException(error);
            return Constants.ERROR_CODE_NO_INFO;
        } else {
            if (BuildConfig.DEBUG) error.printStackTrace();
            if (enable) Crashlytics.logException(error);
            return Constants.ERROR_CODE_INTERNAL;
        }
    }

    public static int resDrawable(short code) {
        return code == Constants.ERROR_CODE_NETWORK ? R.drawable.error_state_disconnected : R.drawable.error_state_cactus;
    }

    public static String message(@NonNull Context context, short code) {
        Resources resources = context.getResources();
        if (Constants.ERROR_CODE_NO_INFO == code) return resources.getString(R.string.error_state_no_info);
        return resources.getString(
                code == Constants.ERROR_CODE_NETWORK ? R.string.error_state_disconnected : R.string.error_state_internal);
    }
}
