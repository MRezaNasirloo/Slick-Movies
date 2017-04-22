package com.github.pedramrn.slick.parent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public class ActivityConductorTest extends AppCompatActivity {

    private Router router;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_test);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.container_test);
        router = Conductor.attachRouter(this, viewGroup, savedInstanceState);
    }

    public Router getRouter() {
        return router;
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
