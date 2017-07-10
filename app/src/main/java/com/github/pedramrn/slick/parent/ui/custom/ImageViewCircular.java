package com.github.pedramrn.slick.parent.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.github.pedramrn.slick.parent.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ImageViewCircular extends AppCompatImageView {
    public ImageViewCircular(Context context) {
        super(context);
    }

    public ImageViewCircular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewCircular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void load(String url) {
        if (url == null) {
            setImageResource(R.drawable.circle);
            return;
        }
        Context context = getContext().getApplicationContext();
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.circle)
                .transform(new CropCircleTransformation())
                .into(this);
    }
}
