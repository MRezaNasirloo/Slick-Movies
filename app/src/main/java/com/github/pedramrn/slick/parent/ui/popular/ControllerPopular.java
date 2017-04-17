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
import com.github.pedramrn.slick.parent.ui.upcoming.RepositoryUserImpl;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class ControllerPopular extends Controller {
    private static final String TAG = ControllerPopular.class.getSimpleName();

    @Inject
    RepositoryUserImpl repositoryUser;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.getMainComponent(getRouter()).inject(this);
        final View view = inflater.inflate(R.layout.controller_popular, container, false);
        final User user = repositoryUser.get("joe").take(1).blockingFirst();

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox_awesome);
        checkBox.setChecked(user.isAwesome());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                repositoryUser.update(user.getBio(), isChecked)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });

        return view;
    }
}
