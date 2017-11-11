package com.github.pedramrn.slick.parent.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.RouterTransaction;

import java.lang.ref.WeakReference;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-09
 */

public class Navigator2 {
    private static WeakReference<Activity> weakReferenceActivity;
    private static WeakReference<Controller> weakReferenceController;

    public static void bind(Activity activity) {
        weakReferenceActivity = new WeakReference<>(activity);
    }

    public static void bind(Controller controller) {
        weakReferenceController = new WeakReference<>(controller);
    }

    public static void unbindActivity() {
        weakReferenceActivity.clear();
    }

    public static void unbindController() {
        weakReferenceController.clear();
    }

    public static void go(Class<? extends Activity> activity) {
        final Activity context = weakReferenceActivity.get();
        if (context != null) {
            context.startActivity(new Intent(context, activity));
        }
    }

    public static void go(@IdRes int id, Fragment fragment) {
        final Activity context = weakReferenceActivity.get();
        if (context != null) {
            context.getFragmentManager().beginTransaction().replace(id, fragment).commit();
        }
    }

    public static void go(Controller controllerNew) {
        Controller controller = weakReferenceController.get();
        if (controller != null) {
            Activity activity = controller.getActivity();
            if (activity != null) {
                activity.runOnUiThread(() -> controller.getRouter().pushController(RouterTransaction.with(controllerNew)));
            }
        }
    }

    public static void go(Controller controllerNew, ControllerChangeHandler pop, ControllerChangeHandler push) {
        Controller controller = weakReferenceController.get();
        if (controller != null) {
            Activity activity = controller.getActivity();
            if (activity != null) {
                activity.runOnUiThread(() -> controller.getRouter().pushController(RouterTransaction.with(controllerNew)
                        .popChangeHandler(pop)
                        .pushChangeHandler(push)
                ));
            }
        }
    }

    @Nullable
    public View viewController() {
        Controller controller = weakReferenceController.get();
        if (controller != null) {
            return controller.getView();
        } else {
            return null;
        }
    }

    @Nullable
    public View viewActivity() {
        Activity activity = weakReferenceActivity.get();
        if (activity != null) {
            return activity.getWindow().getDecorView();
        } else {
            return null;
        }
    }


}