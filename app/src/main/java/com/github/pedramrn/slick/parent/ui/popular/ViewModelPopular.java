package com.github.pedramrn.slick.parent.ui.popular;

import com.github.pedramrn.slick.parent.domain.model.User;
import com.github.pedramrn.slick.parent.ui.upcoming.PresenterUpComing;
import com.github.pedramrn.slick.parent.ui.upcoming.ViewUpComing;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-17
 */

public class ViewModelPopular implements ViewUpComing {
    private static final String TAG = ViewModelPopular.class.getSimpleName();
    private Disposable disposable;

    public ViewModelPopular(final PresenterUpComing presenter) {
        final Observable<User> observable = presenter.getUser("joe").share();

    }


    public void onDestroy() {
        disposable.dispose();
    }
}
