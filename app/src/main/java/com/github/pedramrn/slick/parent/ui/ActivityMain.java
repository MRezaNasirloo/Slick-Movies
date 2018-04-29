package com.github.pedramrn.slick.parent.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bluelinelabs.conductor.Router;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ActivityMainBinding;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryGoogleAuthImpl;
import com.github.pedramrn.slick.parent.exception.NotImplementedException;
import com.github.pedramrn.slick.parent.ui.main.ControllerMain;
import com.mrezanasirloo.slick.middleware.RequestStack;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

public class ActivityMain extends AppCompatActivity implements ToolbarHost {
    private static final String TAG = ActivityMain.class.getSimpleName();

    @Inject
    RepositoryGoogleAuthImpl googleAuth;

    private String CONTROLLER_MAIN_TAG = "ControllerMain_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding view = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Navigator2.bind(this);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentByTag = fm.findFragmentByTag(CONTROLLER_MAIN_TAG);
        if (fragmentByTag == null) {
            fm.beginTransaction()
                    .replace(R.id.controller_container, ControllerMain.newInstance(new Uri.Builder().build()),
                            CONTROLLER_MAIN_TAG)
                    .commit();
        }

        onNewIntent(getIntent());
        App.componentMain().inject(this);
        getLifecycle().addObserver(googleAuth);
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent() called with: intent = [" + intent + "]");
        if (intent != null && intent.getData() != null
                && Intent.ACTION_VIEW.equalsIgnoreCase(intent.getAction())
                && intent.getBooleanExtra("IMDB_URI", true)) {
            Logger.w(TAG, "Starting with intent");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.controller_container, ControllerMain.newInstance(intent.getData()))
                    .commit();
            intent.putExtra("IMDB_URI", false);
            setIntent(intent);// Consumed
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
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(CONTROLLER_MAIN_TAG);
        if (fragmentByTag != null) {
            if (((ControllerMain) fragmentByTag).onBackPressed()) return;
        }
        super.onBackPressed();
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
        // TODO: 2018-04-25 refactor this to an interface
        throw new NotImplementedException("TODO: It needs a router");
    }
}
