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
import com.github.pedramrn.slick.parent.datasource.database.UserEntity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class ControllerPopular extends Controller {
    private static final String TAG = ControllerPopular.class.getSimpleName();

    @Inject
    ReactiveEntityStore<Persistable> entityStore;
    private ReactiveResult<UserEntity> userEntities;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.getMainComponent(getRouter()).inject(this);
        final View view = inflater.inflate(R.layout.controller_popular, container, false);
        final UserEntity userEntity = entityStore.select(UserEntity.class)
                .where(UserEntity.NAME.equal("joe"))
                .get().firstOrNull();

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox_awesome);
        checkBox.setChecked(userEntity.isAwesome());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                entityStore.update(UserEntity.class)
                        .set(UserEntity.AWESOME, isChecked)
                        .where(UserEntity.NAME.equal("joe"))
                        .get()
                        .single()
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });

        entityStore.select(UserEntity.class)
                .where(UserEntity.NAME.equal("joe"))
                .get()
                .observableResult()
                .subscribe(new Consumer<ReactiveResult<UserEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ReactiveResult<UserEntity> userEntities) throws Exception {
                        if (ControllerPopular.this.userEntities != null) {
                            System.out.println(ControllerPopular.this.userEntities.first() == userEntities.first());
                        }
                        ControllerPopular.this.userEntities = userEntities;
                    }
                });

        return view;
    }
}
