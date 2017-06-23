package com.github.pedramrn.slick.parent.utils;

import android.support.test.espresso.Espresso;

import com.github.pedramrn.slick.parent.utils.di.TestApp;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-23
 */

public class OkHttp3IdlingResourcesRule implements TestRule {
    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Espresso.registerIdlingResources(TestApp.getOkHttp3IdlingResource());
                base.evaluate();
                Espresso.unregisterIdlingResources(TestApp.getOkHttp3IdlingResource());

            }
        };
    }
}
