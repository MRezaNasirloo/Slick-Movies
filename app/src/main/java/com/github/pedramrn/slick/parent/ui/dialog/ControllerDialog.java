package com.github.pedramrn.slick.parent.ui.dialog;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.auth.ControllerAuth;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-25
 */

public class ControllerDialog extends Controller {

    private Router childRouter;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_dialog, container, false);
        ViewGroup dialogContainer = view.findViewById(R.id.container_dialog);
        childRouter = getChildRouter(dialogContainer);
        childRouter.pushController(RouterTransaction.with(new ControllerAuth(false)));
        return view;
    }

    public static void start(Router router) {
        router.pushController(RouterTransaction.with(new ControllerDialog())
                .popChangeHandler(new FadeChangeHandler())
                .pushChangeHandler(new FadeChangeHandler()));
    }
}
