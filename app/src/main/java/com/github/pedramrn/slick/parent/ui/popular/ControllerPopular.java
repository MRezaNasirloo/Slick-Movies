package com.github.pedramrn.slick.parent.ui.popular;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.domain.model.User;
import com.github.slick.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

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
        App.getMainComponent(getRouter()).inject(this);
        ControllerPopular_Slick.bind(this);
        final View view = inflater.inflate(R.layout.controller_popular, container, false);

        final CheckBox checkBoxDataBinding = (CheckBox) view.findViewById(R.id.checkBox_data_binding);
        checkBoxDataBinding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
