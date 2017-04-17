package com.github.pedramrn.slick.parent.ui.upcoming;

import android.databinding.ObservableField;

import com.github.pedramrn.slick.parent.domain.model.User;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

import static com.github.pedramrn.slick.parent.databinding.FieldUtils.toField;
import static com.github.pedramrn.slick.parent.databinding.FieldUtils.toObservable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class ViewModelUpComing implements ViewUpComing {
    private static final String TAG = ViewModelUpComing.class.getSimpleName();
    public final ObservableField<String> bio;
    public final ObservableField<Boolean> isAwesome;
    private Disposable disposable;

    public ViewModelUpComing(final PresenterUpComing presenter) {
        final Observable<User> observable = presenter.getUser("joe").share();
        final Observable<String> startBio = observable.map(new Function<User, String>() {
            @Override
            public String apply(@NonNull User userEntity) throws Exception {
                return userEntity.getBio();
            }
        });
        final Observable<Boolean> startAwesome = observable.map(new Function<User, Boolean>() {
            @Override
            public Boolean apply(@NonNull User userEntity) throws Exception {
                return userEntity.isAwesome();
            }
        });

        bio = toField(startBio);
        isAwesome = toField(startAwesome);

        disposable = Observable.combineLatest(
                toObservable(isAwesome).debounce(500, TimeUnit.MILLISECONDS).distinctUntilChanged(),
                toObservable(bio).debounce(500, TimeUnit.MILLISECONDS).distinctUntilChanged(),
                new BiFunction<Boolean, String, Single<Integer>>() {
                    @Override
                    public Single<Integer> apply(@NonNull Boolean awesome, @NonNull String bio) throws Exception {
                        return presenter.updateUser(bio, awesome);
                    }
                }).flatMap(new Function<Single<Integer>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(@NonNull Single<Integer> integerSingle) throws Exception {
                return integerSingle.toObservable();
            }
        })
                .skip(1).subscribe();
    }

    public void onDestroy() {
        disposable.dispose();
    }
}
