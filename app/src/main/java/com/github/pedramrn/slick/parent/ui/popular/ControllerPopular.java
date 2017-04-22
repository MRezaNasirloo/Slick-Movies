package com.github.pedramrn.slick.parent.ui.popular;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.domain.model.User;
import com.github.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.CompletableSource;
import io.reactivex.functions.Function;

import static android.widget.CompoundButton.OnCheckedChangeListener;
import static com.github.pedramrn.slick.parent.App.componentMain;
import static com.github.pedramrn.slick.parent.R.id;
import static com.github.pedramrn.slick.parent.R.id.checkBox_data_binding;
import static com.github.pedramrn.slick.parent.R.layout;
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
        final View view = inflater.inflate(controller_popular, container, false);

        final CheckBox checkBoxDataBinding = (CheckBox) view.findViewById(checkBox_data_binding);
        checkBoxDataBinding.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                presenter.getUser("joe").take(1).flatMapCompletable(new Function<User, CompletableSource>() {
                    @Override
                    public CompletableSource apply(@io.reactivex.annotations.NonNull User user) throws Exception {
                        return presenter.updateUser("change from 3rd page", isChecked).toCompletable();
                    }
                }).subscribe();
            }
        });
        return view;
    }
}
