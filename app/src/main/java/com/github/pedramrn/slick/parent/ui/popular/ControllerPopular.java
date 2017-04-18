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

        final EditText editText = (EditText) view.findViewById(R.id.editText_bio_2);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox_awesome_2);
        final CheckBox checkBoxDataBinding = (CheckBox) view.findViewById(R.id.checkBox_data_binding);
        checkBoxDataBinding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                presenter.getUser("joe").take(1).flatMapCompletable(new Function<User, CompletableSource>() {
                    @Override
                    public CompletableSource apply(@io.reactivex.annotations.NonNull User user) throws Exception {
                        return presenter.updateUser(user.getBio(), isChecked).toCompletable();
                    }
                }).subscribe();
            }
        });

        Observable<User> observable = presenter.getUser("joe");
        final BehaviorSubject<Boolean> subject = BehaviorSubject.create();
        observable.subscribe(new Consumer<User>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull User user) throws Exception {
                subject.onNext(false);

                Log.d(TAG, "accept() called with: user = [" + user + "]");
                if (!user.getBio().equals(editText.getText().toString())) {
                    editText.setText(user.getBio());
                }
                if (checkBox.isChecked() != user.isAwesome()) {
                    checkBox.setChecked(user.isAwesome());
                }
            }
        });

        Observable<Boolean> observableCheckBox = RxCompoundButton.checkedChanges(checkBox).skipInitialValue().debounce(500, TimeUnit.MILLISECONDS);
        Observable<CharSequence> observableEditText = RxTextView.textChanges(editText).skipInitialValue().debounce(500, TimeUnit.MILLISECONDS);

        Observable.combineLatest(observableCheckBox, observableEditText, new BiFunction<Boolean, CharSequence, Single<Integer>>() {
            @Override
            public Single<Integer> apply(@io.reactivex.annotations.NonNull Boolean aBoolean,
                                     @io.reactivex.annotations.NonNull CharSequence text) throws Exception {
                Log.d(TAG, "apply() called with: aBoolean = [" + aBoolean + "], text = [" + text + "]");
                return presenter.updateUser(text.toString(), aBoolean);
            }
        }).filter(new Predicate<Single<Integer>>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Single<Integer> integerSingle) throws Exception {
                boolean oldFromUser = fromUser;
                fromUser = true;
                return oldFromUser;
            }
        }).flatMapCompletable(new Function<Single<Integer>, CompletableSource>() {
            @Override
            public CompletableSource apply(@io.reactivex.annotations.NonNull Single<Integer> integerSingle) throws Exception {
                return integerSingle.toCompletable();
            }
        }).subscribe();

        return view;
    }
}
