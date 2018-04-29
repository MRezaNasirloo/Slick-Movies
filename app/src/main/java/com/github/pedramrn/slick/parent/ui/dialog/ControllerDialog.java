package com.github.pedramrn.slick.parent.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.auth.ControllerAuth;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-11-25
 */

public class ControllerDialog extends DialogFragment {
    public static final String TAG = "DIALOG_LOG_IN";

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        return inflater.inflate(R.layout.controller_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = fm.findFragmentByTag("DIALOG");
        if (fragment == null) {
            fm.beginTransaction()
                    .replace(R.id.container_dialog, ControllerAuth.newInstance(true))
                    .commit();
        }
    }
}
