package com.github.pedramrn.slick.parent.ui.upcoming;

import android.databinding.ObservableField;
import android.util.Log;
import android.util.Pair;

import com.github.pedramrn.slick.parent.domain.model.User;

import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

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
            public String apply(@NonNull User user) throws Exception {
                Log.d(TAG, "apply()1 called with: user = [" + user + "]");
                return user.getBio();
            }
        });
        final Observable<Boolean> startAwesome = observable.map(new Function<User, Boolean>() {
            @Override
            public Boolean apply(@NonNull User user) throws Exception {
                Log.d(TAG, "apply()2 called with: user = [" + user + "]");
                return user.isAwesome();
            }
        });

        bio = toField(startBio);
        isAwesome = toField(startAwesome);

        Observable.combineLatest(
                toObservable(isAwesome).debounce(1, TimeUnit.SECONDS),
                toObservable(bio).debounce(1, TimeUnit.SECONDS),
                new BiFunction<Boolean, String, User>() {
                    @Override
                    public User apply(@NonNull Boolean oldAwesome, @NonNull String oldBio) throws Exception {
                        Log.d(TAG, "apply()3 called with: oldAwesome = [" + oldAwesome + "], oldBio = [" + oldBio + "]");
                        return User.create(-1, bio.get(), isAwesome.get(), "");
                    }
                })
                .withLatestFrom(observable, new BiFunction<User, User, Pair<Boolean, User>>() {
                    @Override
                    public Pair<Boolean, User> apply(@NonNull User user, @NonNull User user2) throws Exception {
                        Log.d(TAG, "apply()4 called with: user = [" + user + "], user2 = [" + user2 + "]");
                        boolean filter = user.getBio().equals(user2.getBio()) && (user.isAwesome() == user2.isAwesome());
                        return new Pair<>(!filter, user);
                    }
                }).filter(new Predicate<Pair<Boolean, User>>() {
            @Override
            public boolean test(@NonNull Pair<Boolean, User> pair) throws Exception {
                return pair.first;
            }
        }).flatMapCompletable(new Function<Pair<Boolean, User>, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull Pair<Boolean, User> pair) throws Exception {
                Log.d(TAG, "apply()5 called");
                return presenter.updateUser(pair.second.getBio(), pair.second.isAwesome()).toCompletable();
            }
        }).subscribe();
        //                ,
        //
        //                .skip(1).subscribe();
    }

    public void onDestroy() {
        disposable.dispose();
    }
}
