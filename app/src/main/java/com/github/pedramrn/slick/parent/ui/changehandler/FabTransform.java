/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Example from https://github.com/nickbutcher/plaid
 */
package com.github.pedramrn.slick.parent.ui.changehandler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * A transition between a FAB & another surface using a circular reveal moving along an arc.
 * <p>
 * See: https://www.google.com/design/spec/motion/transforming-material.html#transforming-material-radial-transformation
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class FabTransform extends Transition {

    private static final long DEFAULT_DURATION = 240L;
    private static final String PROP_BOUNDS = "plaid:fabTransform:bounds";
    private static final String[] TRANSITION_PROPERTIES = {
            PROP_BOUNDS
    };

    private final int color;
    private final int icon;

    public FabTransform(@ColorInt int fabColor, @DrawableRes int fabIconResId) {
        color = fabColor;
        icon = fabIconResId;
        setPathMotion(new GravityArcMotion());
        setDuration(DEFAULT_DURATION);
    }

    @Override
    public String[] getTransitionProperties() {
        return TRANSITION_PROPERTIES;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(final ViewGroup sceneRoot,
                                   final TransitionValues startValues,
                                   final TransitionValues endValues) {
        if (startValues == null || endValues == null)  return null;

        final Rect startBounds = (Rect) startValues.values.get(PROP_BOUNDS);
        final Rect endBounds = (Rect) endValues.values.get(PROP_BOUNDS);

        final boolean fromFab = endBounds.width() > startBounds.width();
        final View view = endValues.view;
        final Rect dialogBounds = fromFab ? endBounds : startBounds;
        final Interpolator fastOutSlowInInterpolator =
                AnimUtils.getFastOutSlowInInterpolator();
        final long duration = getDuration();
        final long halfDuration = duration / 2;
        final long twoThirdsDuration = duration * 2 / 3;

        if (!fromFab) {
            // Force measure / layout the dialog back to it's original bounds
            view.measure(
                    makeMeasureSpec(startBounds.width(), View.MeasureSpec.EXACTLY),
                    makeMeasureSpec(startBounds.height(), View.MeasureSpec.EXACTLY));
            view.layout(startBounds.left, startBounds.top, startBounds.right, startBounds.bottom);
        }

        final int translationX = startBounds.centerX() - endBounds.centerX();
        final int translationY = startBounds.centerY() - endBounds.centerY();
        if (fromFab) {
            view.setTranslationX(translationX);
            view.setTranslationY(translationY);
        }

        // Add a color overlay to fake appearance of the FAB
        final ColorDrawable fabColor = new ColorDrawable(color);
        fabColor.setBounds(0, 0, dialogBounds.width(), dialogBounds.height());
        if (!fromFab) fabColor.setAlpha(0);
        view.getOverlay().add(fabColor);

        // Add an icon overlay again to fake the appearance of the FAB
        final Drawable fabIcon =
                ContextCompat.getDrawable(sceneRoot.getContext(), icon).mutate();
        final int iconLeft = (dialogBounds.width() - fabIcon.getIntrinsicWidth()) / 2;
        final int iconTop = (dialogBounds.height() - fabIcon.getIntrinsicHeight()) / 2;
        fabIcon.setBounds(iconLeft, iconTop,
                iconLeft + fabIcon.getIntrinsicWidth(),
                iconTop + fabIcon.getIntrinsicHeight());
        if (!fromFab) fabIcon.setAlpha(0);
        view.getOverlay().add(fabIcon);

        // Since the view that's being transition to always seems to be on the top (z-order), we have
        // to make a copy of the "from" view and put it in the "to" view's overlay, then fade it out.
        // There has to be another way to do this, right?
        Drawable dialogView = null;
        if (!fromFab) {
            startValues.view.setDrawingCacheEnabled(true);
            startValues.view.buildDrawingCache();
            Bitmap viewBitmap = startValues.view.getDrawingCache();
            dialogView = new BitmapDrawable(view.getResources(), viewBitmap);
            dialogView.setBounds(0, 0, dialogBounds.width(), dialogBounds.height());
            view.getOverlay().add(dialogView);
        }

        // Circular clip from/to the FAB size
        final Animator circularReveal;
        if (fromFab) {
            circularReveal = ViewAnimationUtils.createCircularReveal(view,
                    view.getWidth() / 2,
                    view.getHeight() / 2,
                    startBounds.width() / 2,
                    (float) Math.hypot(endBounds.width() / 2, endBounds.height() / 2));
            circularReveal.setInterpolator(
                    AnimUtils.getFastOutLinearInInterpolator());
        } else {
            circularReveal = ViewAnimationUtils.createCircularReveal(view,
                    view.getWidth() / 2,
                    view.getHeight() / 2,
                    (float) Math.hypot(startBounds.width() / 2, startBounds.height() / 2),
                    endBounds.width() / 2);
            circularReveal.setInterpolator(
                    AnimUtils.getLinearOutSlowInInterpolator());

            // Persist the end clip i.e. stay at FAB size after the reveal has run
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    final ViewOutlineProvider fabOutlineProvider = view.getOutlineProvider();

                    view.setOutlineProvider(new ViewOutlineProvider() {
                        boolean hasRun = false;

                        @Override
                        public void getOutline(final View view, Outline outline) {
                            final int left = (view.getWidth() - endBounds.width()) / 2;
                            final int top = (view.getHeight() - endBounds.height()) / 2;

                            outline.setOval(
                                    left, top, left + endBounds.width(), top + endBounds.height());

                            if (!hasRun) {
                                hasRun = true;
                                view.setClipToOutline(true);

                                // We have to remove this as soon as it's laid out so we can boxOffice the shadow back
                                view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw() {
                                        if (view.getWidth() == endBounds.width() && view.getHeight() == endBounds.height()) {
                                            view.setOutlineProvider(fabOutlineProvider);
                                            view.setClipToOutline(false);
                                            view.getViewTreeObserver().removeOnPreDrawListener(this);
                                            return true;
                                        }

                                        return true;
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
        circularReveal.setDuration(duration);

        // Translate to end position along an arc
        final Animator translate = ObjectAnimator.ofFloat(
                view,
                View.TRANSLATION_X,
                View.TRANSLATION_Y,
                fromFab ? getPathMotion().getPath(translationX, translationY, 0, 0)
                        : getPathMotion().getPath(0, 0, -translationX, -translationY));
        translate.setDuration(duration);
        translate.setInterpolator(fastOutSlowInInterpolator);

        // Fade contents of non-FAB view in/out
        List<Animator> fadeContents = null;
        if (view instanceof ViewGroup) {
            final ViewGroup vg = ((ViewGroup) view);
            fadeContents = new ArrayList<>(vg.getChildCount());
            for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                final View child = vg.getChildAt(i);
                final Animator fade =
                        ObjectAnimator.ofFloat(child, View.ALPHA, fromFab ? 1f : 0f);
                if (fromFab) {
                    child.setAlpha(0f);
                }
                fade.setDuration(twoThirdsDuration);
                fade.setInterpolator(fastOutSlowInInterpolator);
                fadeContents.add(fade);
            }
        }

        // Fade in/out the fab color & icon overlays
        final Animator colorFade = ObjectAnimator.ofInt(fabColor, "alpha", fromFab ? 0 : 255);
        final Animator iconFade = ObjectAnimator.ofInt(fabIcon, "alpha", fromFab ? 0 : 255);
        if (!fromFab) {
            colorFade.setStartDelay(halfDuration);
            iconFade.setStartDelay(halfDuration);
        }
        colorFade.setDuration(halfDuration);
        iconFade.setDuration(halfDuration);
        colorFade.setInterpolator(fastOutSlowInInterpolator);
        iconFade.setInterpolator(fastOutSlowInInterpolator);

        // Run all animations together
        final AnimatorSet transition = new AnimatorSet();
        transition.playTogether(circularReveal, translate, colorFade, iconFade);
        transition.playTogether(fadeContents);
        if (dialogView != null) {
            final Animator dialogViewFade = ObjectAnimator.ofInt(dialogView, "alpha", 0).setDuration(twoThirdsDuration);
            dialogViewFade.setInterpolator(fastOutSlowInInterpolator);
            transition.playTogether(dialogViewFade);
        }
        transition.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Clean up
                view.getOverlay().clear();

                if (!fromFab) {
                    view.setTranslationX(0);
                    view.setTranslationY(0);
                    view.setTranslationZ(0);

                    view.measure(
                            makeMeasureSpec(endBounds.width(), View.MeasureSpec.EXACTLY),
                            makeMeasureSpec(endBounds.height(), View.MeasureSpec.EXACTLY));
                    view.layout(endBounds.left, endBounds.top, endBounds.right, endBounds.bottom);
                }

            }
        });
        return new AnimUtils.NoPauseAnimator(transition);
    }

    private void captureValues(TransitionValues transitionValues) {
        final View view = transitionValues.view;
        if (view == null || view.getWidth() <= 0 || view.getHeight() <= 0) return;

        transitionValues.values.put(PROP_BOUNDS, new Rect(view.getLeft(), view.getTop(),
                view.getRight(), view.getBottom()));
    }
}
