package com.github.pedramrn.slick.parent.ui.login;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.App;
import com.github.pedramrn.slick.parent.ui.main.RouterLogin;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public class ControllerLogin extends Controller {
    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        final View view = layoutInflater.inflate(R.layout.controller_login, viewGroup, false);
        view.findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ControllerLogin.this.getActivity(), "Dude it's a demo.", Toast.LENGTH_SHORT).show();
                App.loggedIn = true;
                new RouterLogin(getRouter()).finish();
            }
        });
        return view;
    }
}
