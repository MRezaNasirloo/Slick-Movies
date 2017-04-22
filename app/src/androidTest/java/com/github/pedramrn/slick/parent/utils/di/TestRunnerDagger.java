package com.github.pedramrn.slick.parent.utils.di;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public class TestRunnerDagger extends AndroidJUnitRunner {

    @Override
    @NonNull
    public Application newApplication(@NonNull ClassLoader cl, @NonNull String className, @NonNull Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return Instrumentation.newApplication(TestApp.class, context);
    }
}
