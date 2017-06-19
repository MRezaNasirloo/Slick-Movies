package com.github.pedramrn.slick.parent.ui.popular;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.github.pedramrn.slick.parent.App.componentMain;
import static com.github.pedramrn.slick.parent.R.layout.controller_popular;
import static com.github.pedramrn.slick.parent.ui.popular.ControllerPopular_Slick.bind;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class ControllerPopular extends Controller implements ViewPopular {
    private static final String TAG = ControllerPopular.class.getSimpleName();

    @Inject
    Provider<PresenterPopular> provider;

    @Presenter
    PresenterPopular presenter;

    private boolean fromUser = false;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        componentMain().inject(this);
        bind(this);
        return inflater.inflate(R.layout.controller_popular, container, false);
    }
}
