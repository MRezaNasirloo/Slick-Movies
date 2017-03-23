package com.github.pedramrn.slick.parent.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.pedramrn.slick.parent.R;

public class SimpleActivity extends AppCompatActivity {

    private static final String TAG = SimpleActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final long before = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        Log.e(TAG, "It took for simple activity:" + (System.currentTimeMillis() - before));
    }
}
