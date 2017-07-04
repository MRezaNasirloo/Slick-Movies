package com.github.pedramrn.slick.parent.ui.custom;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.github.pedramrn.slick.parent.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ImageViewGlide extends AppCompatImageView {
    public ImageViewGlide(Context context) {
        super(context);
    }

    public ImageViewGlide(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewGlide(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void load(String url) {
        if (url == null) {
            return;
        }
        Context context = getContext().getApplicationContext();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.rectangle_no_corners)
                .crossFade()
                .into(this);
    }

    public void load(String url, @DimenRes int dimen) {
        if (url == null) {
            return;
        }
        Context context = getContext().getApplicationContext();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.rectangle)
                .crossFade()
                .bitmapTransform(new RoundedCornersTransformation(context,
                        context.getResources().getDimensionPixelSize(dimen), 0))
                .into(this);
    }
}
