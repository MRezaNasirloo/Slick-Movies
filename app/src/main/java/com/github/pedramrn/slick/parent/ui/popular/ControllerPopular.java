package com.github.pedramrn.slick.parent.ui.popular;

import android.support.annotation.NonNull;
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
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewBeforeTextChangeEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.getMainComponent(getRouter()).inject(this);
        ControllerPopular_Slick.bind(this);
        final View view = inflater.inflate(R.layout.controller_popular, container, false);

        final EditText editText = (EditText) view.findViewById(R.id.editText_bio_2);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox_awesome_2);
        final CheckBox checkBoxDataBinding = (CheckBox) view.findViewById(R.id.checkBox_data_binding);
        checkBoxDataBinding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.updateUser(buttonView.getText().toString(), checkBox.isChecked())
                        .subscribe();
            }
        });

        presenter.getUser("joe").subscribe(new Consumer<User>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull User user) throws Exception {
                if (!user.getBio().equals(editText.getText().toString())) {
                    editText.setText(user.getBio());
                }
                if (checkBox.isChecked() != user.isAwesome()) {
                    checkBox.setChecked(user.isAwesome());
                }
            }
        });

        Observable<Boolean> observableCheckBox = RxCompoundButton.checkedChanges(checkBox).skipInitialValue().skip(1);
        Observable<String> observableEditText = RxTextView.beforeTextChangeEvents(editText).skipInitialValue().skip(1)
                .map(new Function<TextViewBeforeTextChangeEvent, String>() {
                    @Override
                    public String apply(
                            @io.reactivex.annotations.NonNull TextViewBeforeTextChangeEvent textViewBeforeTextChangeEvent) throws Exception {
                        return textViewBeforeTextChangeEvent.text().toString();
                    }
                });

        Observable.combineLatest(observableCheckBox, observableEditText, new BiFunction<Boolean, String, Single<Integer>>() {
            @Override
            public Single<Integer> apply(@io.reactivex.annotations.NonNull Boolean aBoolean,
                                         @io.reactivex.annotations.NonNull String text) throws Exception {
                return presenter.updateUser(text, aBoolean);
            }
        }).flatMap(new Function<Single<Integer>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(@io.reactivex.annotations.NonNull Single<Integer> integerSingle) throws Exception {
                return integerSingle.toObservable();
            }
        }).skip(1).subscribe();

        return view;
    }
}
