package com.github.pedramrn.slick.parent.ui.changehandler;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler;
import com.github.pedramrn.slick.parent.ui.custom.OnCompleteGlide;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ArcFadeMoveChangeHandlerDelaying extends TransitionChangeHandler implements OnCompleteGlide {

    private OnTransitionPreparedListener onTransitionPreparedListener;

    public ArcFadeMoveChangeHandlerDelaying() {
    }

    private static final String TAG = ArcFadeMoveChangeHandlerDelaying.class.getSimpleName();

    @Override
    @NonNull
    protected Transition getTransition(@NonNull ViewGroup container, View from, View to, boolean isPush) {
        TransitionSet transition = new TransitionSet();
        transition.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        transition
                .addTransition(new Fade(Fade.OUT))
                .addTransition(new TransitionSet()
                        .addTransition(new ChangeTransform())
                        .addTransition(new ChangeBounds())
                        .addTransition(new ChangeClipBounds())
                        .addTransition(new ChangeImageTransform()))
                .addTransition(new Fade(Fade.IN))
        ;

        return transition;
    }

    @Override
    public void prepareForTransition(@NonNull ViewGroup container, @Nullable View from, @Nullable View to, @NonNull Transition transition,
                                     boolean isPush, @NonNull OnTransitionPreparedListener onTransitionPreparedListener) {
        this.onTransitionPreparedListener = onTransitionPreparedListener;
    }

    @Override
    public void onCompleteGlide() {
        onTransitionPreparedListener.onPrepared();
    }
}
