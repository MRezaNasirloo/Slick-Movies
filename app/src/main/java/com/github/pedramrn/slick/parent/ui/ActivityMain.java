package com.github.pedramrn.slick.parent.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.ActivityMainBinding;
import com.github.pedramrn.slick.parent.ui.main.ControllerMain;

public class ActivityMain extends AppCompatActivity implements ToolbarHost {
    private static final String TAG = ActivityMain.class.getSimpleName();

    private Router router;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding view = DataBindingUtil.setContentView(this, R.layout.activity_main);
        router = Conductor.attachRouter(this, view.controllerContainer, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new ControllerMain()));
        }
    }

    @Override
    public void onBackPressed() {
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
        if (isFinishing()) {
            App.disposeComponentMain();
        }
        super.onDestroy();
    }

    @Override
    public ToolbarHost setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        setSupportActionBar(this.toolbar);
        return this;
    }

    @Override
    public ToolbarHost setupButton(boolean enable) {
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return this;
    }
}
