package com.github.pedramrn.slick.parent.ui.changehandler;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.*;
import android.transition.Transition.TransitionListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bluelinelabs.conductor.changehandler.SharedElementTransitionChangeHandler;
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler;
import com.bluelinelabs.conductor.internal.TransitionUtils;

import java.util.ArrayList;
import java.util.Collections;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ArcFadeMoveChangeHandlerOld  extends TransitionChangeHandler {


    public ArcFadeMoveChangeHandlerOld() {
    }

    @Override
    @NonNull
    protected Transition getTransition(@NonNull ViewGroup container, View from, View to, boolean isPush) {
        TransitionSet transition = new TransitionSet()
                .setOrdering(TransitionSet.ORDERING_SEQUENTIAL)
                .addTransition(new Fade(Fade.OUT))

                .addTransition(new TransitionSet()
                        .addTransition(new ChangeTransform())
                        .addTransition(new ChangeBounds())
                        .addTransition(new ChangeClipBounds())
                        .addTransition(new android.transition.ChangeImageTransform()))
                .addTransition(new Fade(Fade.IN));

        transition.setPathMotion(new ArcMotion());

        return transition;
    }
}
