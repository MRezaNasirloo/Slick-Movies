package com.github.pedramrn.slick.parent.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.library.middleware.RequestStack;
import com.github.pedramrn.slick.parent.ui.main.HomeController;

public class MainActivity extends AppCompatActivity {

    private Router router;
    RequestStack requestStack = RequestStack.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup container = (ViewGroup) findViewById(R.id.controller_container);

        router = Conductor.attachRouter(this, container, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new HomeController()));
        }
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onResume() {
        requestStack.onResume(this);
        super.onResume();
        Log.e(TAG, "onStart() called " + isChangingConfigurations());
    }

    @Override
    protected void onPause() {
        requestStack.onPause(this);
        super.onPause();
        Log.e(TAG, "onStop() called " + isChangingConfigurations());
    }

    @Override
    public void onBackPressed() {
        requestStack.handle();
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
