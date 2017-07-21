package com.github.pedramrn.slick.parent.ui.upcoming;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.databinding.ControllerUpComingBinding;
import com.github.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.github.pedramrn.slick.parent.databinding.ControllerUpComingBinding.inflate;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class ControllerUpComing extends Controller implements ViewUpComing {


    @Inject
    Provider<PresenterUpComing> provider;
    @Presenter
    PresenterUpComing presenter;

    // ViewModelUpComing viewModelUpComing;
    // @Inject
    // ReactiveEntityStore<Persistable> entityStore;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        // componentMain().inject(this);
        ControllerUpComing_Slick.bind(this);

        /*if (viewModelUpComing == null) {
            viewModelUpComing = new ViewModelUpComing(presenter);
        }*/


        final ControllerUpComingBinding binding = inflate(inflater, container, false);

        return binding.getRoot();
    }
}
