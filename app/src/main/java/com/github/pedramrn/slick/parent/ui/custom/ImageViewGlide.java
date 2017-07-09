package com.github.pedramrn.slick.parent.ui.custom;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.pedramrn.slick.parent.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ImageViewGlide extends AppCompatImageView {
    private String url;

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

    public void loadNoFade(String url) {
        if (url == null) {
            return;
        }
        Context context = getContext().getApplicationContext();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.rectangle_no_corners)
                .into(this);
    }

    public void loadForSE(String url, final OnCompleteGlide listener) {
        if (url == null) {
            return;
        }
        CompletableSubject subject = CompletableSubject.create();
        subject.ambWith(Observable.just(1).delay(500, TimeUnit.MILLISECONDS).ignoreElements())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        listener.onCompleteGlide();
                    }
                });
        Context context = getContext().getApplicationContext();
        Glide.with(context)
                .load(url)
                // .dontTransform()
                // .skipMemoryCache(true)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e,
                                               String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        Log.d(TAG, "onException() called with: e = [" +
                                e +
                                "], model = [" +
                                model +
                                "], target = [" +
                                target +
                                "], isFirstResource = [" +
                                isFirstResource +
                                "]");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        listener.onCompleteGlide();
                        Log.d(TAG, "onResourceReady() called with: resource = [" +
                                resource +
                                "], model = [" +
                                model +
                                "], target = [" +
                                target +
                                "], isFromMemoryCache = [" +
                                isFromMemoryCache +
                                "], isFirstResource = [" +
                                isFirstResource +
                                "]");
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
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

    private static final String TAG = ImageViewGlide.class.getSimpleName();

    public void delayLoadForSE(String url) {
        this.url = url;
    }

    public void startDelayedLoadForSE(final OnCompleteGlide onComplete) {
        if (url == null) {
            return;
        }
        CompletableSubject subject = CompletableSubject.create();
        subject.ambWith(Observable.just(1).delay(500, TimeUnit.MILLISECONDS).ignoreElements())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        onComplete.onCompleteGlide();
                    }
                });
        Context context = getContext().getApplicationContext();
        Glide.with(context)
                .load(url)
                // .dontTransform()
                // .skipMemoryCache(true)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e,
                                               String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        Log.d(TAG, "onException() called with: e = [" +
                                e +
                                "], model = [" +
                                model +
                                "], target = [" +
                                target +
                                "], isFirstResource = [" +
                                isFirstResource +
                                "]");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        onComplete.onCompleteGlide();
                        Log.d(TAG, "onResourceReady() called with: resource = [" +
                                resource +
                                "], model = [" +
                                model +
                                "], target = [" +
                                target +
                                "], isFromMemoryCache = [" +
                                isFromMemoryCache +
                                "], isFirstResource = [" +
                                isFirstResource +
                                "]");
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(this);

    }
}
