/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.github.pedramrn.slick.parent.ui.changehandler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.Drawable;
import android.transition.TransitionValues;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * This Transition captures an ImageView's matrix before and after the
 * scene change and animates it during the transition.
 * <p>
 * <p>In combination with ChangeBounds, ChangeImageTransform allows ImageViews
 * that change size, shape, or {@link ImageView.ScaleType} to animate contents
 * smoothly.</p>
 */
public class ChangeImageTransform extends android.transition.ChangeImageTransform {

    /*private static final String TAG = "ChangeImageTransform";

    private static final String PROPNAME_MATRIX = "android:changeImageTransform:matrix";
    private static final String PROPNAME_BOUNDS = "android:changeImageTransform:bounds";

    private static final String[] sTransitionProperties = {
            PROPNAME_MATRIX,
            PROPNAME_BOUNDS,
    };

    private static TypeEvaluator<Matrix> NULL_MATRIX_EVALUATOR = new TypeEvaluator<Matrix>() {
        @Override
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            return null;
        }
    };

    private static Property<ImageView, Matrix> ANIMATED_TRANSFORM_PROPERTY
            = new Property<ImageView, Matrix>(Matrix.class, "animatedTransform") {
        @Override
        public void set(ImageView object, Matrix value) {
            object.animateTransform(value);
        }

        @Override
        public Matrix getFull(ImageView object) {
            return null;
        }
    };

    public ChangeImageTransform() {
    }

    public ChangeImageTransform(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (!(view instanceof ImageView) || view.getVisibility() != View.VISIBLE) {
            return;
        }
        ImageView imageView = (ImageView) view;
        Drawable drawableStart = imageView.getDrawable();
        if (drawableStart == null) {
            return;
        }
        Map<String, Object> values = transitionValues.values;

        int left = view.getLeft();
        int top = view.getTop();
        int right = view.getRight();
        int bottom = view.getBottom();

        Rect bounds = new Rect(left, top, right, bottom);
        values.put(PROPNAME_BOUNDS, bounds);
        Matrix matrix;
        ImageView.ScaleType scaleType = imageView.getScaleType();
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            matrix = imageView.getImageMatrix();
            if (!matrix.isIdentity()) {
                matrix = new Matrix(matrix);
            } else {
                int drawableWidth = drawableStart.getIntrinsicWidth();
                int drawableHeight = drawableStart.getIntrinsicHeight();
                if (drawableWidth > 0 && drawableHeight > 0) {
                    float scaleX = ((float) bounds.width()) / drawableWidth;
                    float scaleY = ((float) bounds.height()) / drawableHeight;
                    matrix = new Matrix();
                    matrix.setScale(scaleX, scaleY);
                } else {
                    matrix = null;
                }
            }
        } else {
            matrix = new Matrix(imageView.getImageMatrix());
        }
        values.put(PROPNAME_MATRIX, matrix);
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
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    *//**
     * Creates an Animator for ImageViews moving, changing dimensions, and/or changing
     * {@link ImageView.ScaleType}.
     *
     * @param sceneRoot   The root of the transition hierarchy.
     * @param startValues The values for a specific target in the start scene.
     * @param endValues   The values for the target in the end scene.
     * @return An Animator to move an ImageView or null if the View is not an ImageView,
     * the Drawable changed, the View is not VISIBLE, or there was no change.
     *//*
    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Rect startBounds = (Rect) startValues.values.movieFull(PROPNAME_BOUNDS);
        Rect endBounds = (Rect) endValues.values.movieFull(PROPNAME_BOUNDS);
        if (startBounds == null || endBounds == null) {
            return null;
        }

        Matrix startMatrix = (Matrix) startValues.values.movieFull(PROPNAME_MATRIX);
        Matrix endMatrix = (Matrix) endValues.values.movieFull(PROPNAME_MATRIX);

        boolean matricesEqual = (startMatrix == null && endMatrix == null) ||
                (startMatrix != null && startMatrix.equals(endMatrix));

        if (startBounds.equals(endBounds) && matricesEqual) {
            return null;
        }

        ImageView imageViewStart = (ImageView) startValues.view;
        ImageView imageView = (ImageView) endValues.view;
        Drawable drawableStart = imageView.getDrawable();
        int drawableWidth = drawableStart.getIntrinsicWidth();
        int drawableHeight = drawableStart.getIntrinsicHeight();

        ObjectAnimator animator;
        if (drawableWidth == 0 || drawableHeight == 0) {
            animator = createNullAnimator(imageView);
        } else {
            if (startMatrix == null) {
                startMatrix = Matrix.IDENTITY_MATRIX;
            }
            if (endMatrix == null) {
                endMatrix = Matrix.IDENTITY_MATRIX;
            }
            ANIMATED_TRANSFORM_PROPERTY.set(imageView, startMatrix);
            animator = createMatrixAnimator(imageView, startMatrix, endMatrix);
        }
        return animator;
    }

    private ObjectAnimator createNullAnimator(ImageView imageView) {
        return ObjectAnimator.ofObject(imageView, ANIMATED_TRANSFORM_PROPERTY,
                NULL_MATRIX_EVALUATOR, null, null);
    }

    private ObjectAnimator createMatrixAnimator(final ImageView imageView, Matrix startMatrix,
                                                final Matrix endMatrix) {
        return ObjectAnimator.ofObject(imageView, ANIMATED_TRANSFORM_PROPERTY,
                new TransitionUtils.MatrixEvaluator(), startMatrix, endMatrix);
    }*/

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, final TransitionValues startValues, final TransitionValues endValues) {
        final Animator animator = super.createAnimator(sceneRoot, startValues, endValues);
        if (animator == null) return null;
        animator.addListener(new AnimatorListenerAdapter() {

            public Drawable drawableEnd;
            private Drawable drawableStart;

            @Override
            public void onAnimationEnd(Animator animation) {
                ((ImageView) endValues.view).setImageDrawable(drawableEnd);
                animator.removeListener(this);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                drawableStart = ((ImageView) startValues.view).getDrawable();
                drawableEnd = ((ImageView) endValues.view).getDrawable();
                ((ImageView) endValues.view).setImageDrawable(drawableStart);
            }
        });
        return animator;
    }
}
