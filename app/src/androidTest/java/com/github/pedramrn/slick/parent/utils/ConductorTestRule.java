package com.github.pedramrn.slick.parent.utils;


import android.support.test.rule.ActivityTestRule;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.ActivityConductorTest;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public class ConductorTestRule<T extends Controller> extends ActivityTestRule<ActivityConductorTest> {
    private final Controller controller;

    public ConductorTestRule(Controller controller) {
        super(ActivityConductorTest.class);
        this.controller = controller;
    }

    public ConductorTestRule(Controller controller, boolean initialTouchMode) {
        super(ActivityConductorTest.class, initialTouchMode);
        this.controller = controller;
    }

    public ConductorTestRule(Controller controller, boolean initialTouchMode, boolean launchActivity) {
        super(ActivityConductorTest.class, initialTouchMode, launchActivity);
        this.controller = controller;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!getActivity().getRouter().hasRootController()) {
                    getActivity().getRouter().setRoot(RouterTransaction.with(controller));
                }
            }
        });
    }
}
