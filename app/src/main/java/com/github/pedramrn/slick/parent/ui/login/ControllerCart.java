package com.github.pedramrn.slick.parent.ui.login;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public class ControllerCart extends Controller {
    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        final View view = layoutInflater.inflate(R.layout.controller_cart, viewGroup, false);
        view.findViewById(R.id.imageView_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ControllerCart.this.getActivity(), "Nice cart ha?.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
