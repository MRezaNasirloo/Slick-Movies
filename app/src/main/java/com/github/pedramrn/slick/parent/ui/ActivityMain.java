package com.github.pedramrn.slick.parent.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ActivityMainBinding;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryGoogleAuthImpl;
import com.github.pedramrn.slick.parent.ui.main.ControllerMain;
import com.github.slick.middleware.RequestStack;

import javax.inject.Inject;

public class ActivityMain extends AppCompatActivity implements ToolbarHost {
    private static final String TAG = ActivityMain.class.getSimpleName();

    @Inject
    RepositoryGoogleAuthImpl googleAuth;

    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding view = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Navigator2.bind(this);
        router = Conductor.attachRouter(this, view.controllerContainer, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new ControllerMain()));
        }
        App.componentMain().inject(this);
        getLifecycle().addObserver(googleAuth);
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
