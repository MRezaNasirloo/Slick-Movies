package com.github.pedramrn.slick.parent.ui.image;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerImageBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerElm;
import com.github.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Controller} subclass.
 */
public class ControllerImage extends ControllerElm<ViewStateImage> implements ViewImage {

    @Inject
    Provider<PresenterImage> provider;
    @Presenter
    PresenterImage presenter;

    public ControllerImage(@Nullable Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        // TODO: 2017-07-22 Inject dependencies 
        App.componentMain().inject(this);
        ControllerImage_Slick.bind(this);
        ControllerImageBinding binding = ControllerImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onSubscribe(Disposable d) {
        add(d);
    }

    @Override
    public void onNext(ViewStateImage viewStateImage) {
        // TODO: 2017-09-06
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete() called");
    }

    private static final String TAG = ControllerImage.class.getSimpleName();
}
