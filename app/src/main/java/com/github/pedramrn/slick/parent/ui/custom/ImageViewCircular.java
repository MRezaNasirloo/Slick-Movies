package com.github.pedramrn.slick.parent.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.github.pedramrn.slick.parent.R;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-16
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

    public void load(String url, Runnable callback) {
        if (url == null) {
            // TODO: 2017-11-11 do this for all
            setImageResource(R.drawable.circle);
            return;
        }
        Context context = getContext();
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.circle)
                .transform(new CropCircleTransformation())
                .into(this, new Callback() {
                    @Override
                    public void onSuccess() {
                        callback.run();
                    }

                    @Override
                    public void onError() {
                        callback.run();
                    }
                });
    }

    public void load(String thumbnailUrl, String url) {
        if (url == null || thumbnailUrl == null) {
            // TODO: 2017-11-11 do this for all
            Logger.e("load: something was null");
            setImageResource(R.drawable.circle);
            return;
        }
        Context context = getContext();
        Picasso.with(context)
                .load(thumbnailUrl)
                .transform(new BlurTransformation(context))
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.circle)
                .into(this, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context)
                                .load(url)
                                .placeholder(getDrawable())
                                .transform(new CropCircleTransformation())
                                .into(ImageViewCircular.this);
                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(R.drawable.error_state_car)
                                .transform(new CropCircleTransformation())
                                .into(ImageViewCircular.this);
                        // setOnClickListener(v -> load(thumbnailUrl, url));
                    }
                });
    }

    /**
     * Loads url without placeholder
     *
     * @param url the image url
     */
    public void loadNP(String url) {
        if (url == null) {
            setImageResource(R.drawable.circle);
            return;
        }
        Context context = getContext().getApplicationContext();
        Picasso.with(context)
                .load(url)
                .noPlaceholder()
                .transform(new CropCircleTransformation())
                .into(this);
    }
}
