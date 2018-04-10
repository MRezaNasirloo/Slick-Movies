package com.github.pedramrn.slick.parent.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ActivityMainBinding;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryGoogleAuthImpl;
import com.github.pedramrn.slick.parent.ui.main.ControllerMain;
import com.mrezanasirloo.slick.middleware.RequestStack;

import javax.inject.Inject;

public class ActivityMain extends AppCompatActivity implements ToolbarHost {
    private static final String TAG = ActivityMain.class.getSimpleName();

    @Inject
    RepositoryGoogleAuthImpl googleAuth;

    private Router router;
    private String CONTROLLER_MAIN_TAG = "ControllerMain_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding view = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Navigator2.bind(this);
        router = Conductor.attachRouter(this, view.controllerContainer, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new ControllerMain(new Uri.Builder().build())));
        }
        onNewIntent(getIntent());
        App.componentMain().inject(this);
        getLifecycle().addObserver(googleAuth);
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent() called with: intent = [" + intent + "]");
        if (intent != null && intent.getData() != null) {
            router.setRoot(RouterTransaction.with(new ControllerMain(intent.getData())));
            setIntent(null);// Consumed
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent() called with: intent = [" + intent + "]");
        super.onNewIntent(intent);
        handleIntent(intent);
        setIntent(null);
    }

    @Override
    public void onBackPressed() {
        RequestStack.getInstance().handleBack();
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        Navigator2.unbindActivity();
        if (isFinishing()) {
            getLifecycle().removeObserver(googleAuth);
            App.disposeComponentMain();
            App.disposeComponentApp();
        }
        // this.toolbar.setNavigationOnClickListener(null);
        super.onDestroy();
    }

    @Override
    public ToolbarHost setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        return this;
    }

    @Override
    public ToolbarHost setupButton(Toolbar toolbar, boolean enable) {
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        return this;
    }

    public Router getRouter() {
        return router;
    }
}
