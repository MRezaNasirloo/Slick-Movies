package com.github.pedramrn.slick.parent.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.main.HomeController;

public class MainActivity extends AppCompatActivity {

    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final long before = System.currentTimeMillis();
        setContentView(R.layout.activity_main);
        ViewGroup container = (ViewGroup) findViewById(R.id.controller_container);

        Log.e(TAG, "It took for main:" + (System.currentTimeMillis() - before));
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new HomeController()));
        }
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onStart() called " + isChangingConfigurations());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onStop() called " + isChangingConfigurations());
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            App.disposeMainComponent();
        }
    }
}
