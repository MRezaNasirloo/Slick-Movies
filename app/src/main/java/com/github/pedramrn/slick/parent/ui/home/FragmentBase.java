package com.github.pedramrn.slick.parent.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.Screen;
import com.github.pedramrn.slick.parent.ui.SnackbarManager;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;
import com.github.pedramrn.slick.parent.ui.details.ErrorHandlerSnackbar;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.mrezanasirloo.slick.SlickUniqueId;
import com.orhanobut.logger.Logger;

import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import static com.mrezanasirloo.slick.SlickDelegateActivity.SLICK_UNIQUE_KEY;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 * Created on: 2018-04-25
 */
public abstract class FragmentBase extends Fragment implements SlickUniqueId, Screen, ToolbarHost, Navigator {

    private static final String TAG = FragmentBase.class.getSimpleName();
    protected final PublishSubject<Object> errorDismissed = PublishSubject.create();
    protected final PublishSubject<Object> retry = PublishSubject.create();
    private CompositeDisposable compositeDisposable;
    private ErrorHandlerSnackbar snackbar;
    private String id;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            id = savedInstanceState.getString(SLICK_UNIQUE_KEY);
        }
        compositeDisposable = new CompositeDisposable();
        setScreenTransition(getScreenTransition());
    }

    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        snackbar = new ErrorHandlerSnackbar(view, "Retry") {
            @Override
            public void onDismissed(int event) {
                errorDismissed.onNext(1);
                if (Snackbar.Callback.DISMISS_EVENT_ACTION == event) {
                    retry.onNext(1);
                }
            }
        };
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        setToolbar(null);
        snackbar = null;
        UtilsRx.dispose(compositeDisposable);
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        App.refWatcher(getActivity()).watch(this);
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SLICK_UNIQUE_KEY, id);
    }

    @NonNull
    @Override
    public String getUniqueId() {
        return id = id != null ? id : UUID.randomUUID().toString();
    }

    public String getInstanceId() {
        return id;
    }

    @Override
    public void navigateTo(@NonNull Screen screen) {
        if (screen instanceof Fragment) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ((Fragment) screen))
                    .addToBackStack(screen.toString())
                    .commit();
        }
    }

    @Override
    public void navigateTo(@NonNull Screen screen, View sharedView, String transitionName) {
        Logger.e("navigateTo() called with: screen = [" + screen + "], sharedView = [" + sharedView + "], transitionName = ["
                + transitionName + "]");
        screen.setScreenTransition(screen.getScreenTransition());
        getFragmentManager()
                .beginTransaction()
                .addSharedElement(sharedView, transitionName)
                .replace(R.id.fragment_container, ((Fragment) screen), "tag")
                .addToBackStack(null)
                .commit();

    }

    @NonNull
    @Override
    public SnackbarManager snackbarManager() {
        return message -> snackbar.show(message);
    }


    public ToolbarHost setToolbar(Toolbar toolbar) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof ToolbarHost) {
            return ((ToolbarHost) activity).setToolbar(toolbar);
        }
        //emulates no-op impl
        return this;
    }


    @Override
    public ToolbarHost setupButton(Toolbar toolbar, boolean enable) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof ToolbarHost) {
            return ((ToolbarHost) activity).setupButton(toolbar, enable);
        }//emulates no-op impl
        return this;
    }

    protected void renderError(@Nullable Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
            Toast.makeText(this.getContext().getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void add(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}