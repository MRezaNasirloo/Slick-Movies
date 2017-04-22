package com.github.pedramrn.slick.parent.ui.upcoming;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerUpComingBinding;
import com.github.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Provider;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

import static android.widget.CompoundButton.OnCheckedChangeListener;
import static com.github.pedramrn.slick.parent.App.componentMain;
import static com.github.pedramrn.slick.parent.databinding.ControllerUpComingBinding.inflate;
import static com.github.pedramrn.slick.parent.ui.upcoming.ControllerUpComing_Slick.bind;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class ControllerUpComing extends Controller implements ViewUpComing {


    @Inject
    Provider<PresenterUpComing> provider;
    @Presenter
    PresenterUpComing presenter;

        ViewModelUpComing viewModelUpComing;
    @Inject
    ReactiveEntityStore<Persistable> entityStore;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        componentMain().inject(this);
        bind(this);

        if (viewModelUpComing == null) {
            viewModelUpComing = new ViewModelUpComing(presenter);
        }


        final ControllerUpComingBinding binding = inflate(inflater, container, false);
        binding.setViewModelUser(viewModelUpComing);

        binding.checkBoxShouldBeSame.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.updateUser("from checkbox up there ^", isChecked).toCompletable().subscribe();
            }
        });

        return binding.getRoot();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        viewModelUpComing.onDestroy();
    }
}
