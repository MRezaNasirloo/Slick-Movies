package com.github.pedramrn.slick.parent.ui.custom;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.github.pedramrn.slick.parent.R;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.CompletableSubject;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ImageViewLoader extends AppCompatImageView {
    private boolean mock = false;
    // private boolean debug = BuildConfig.DEBUG;
    private boolean debug = false;

    public ImageViewLoader(Context context) {
        super(context);
    }

    public ImageViewLoader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewLoader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void load(String url) {
        if (mock) {
            setBackgroundResource(R.drawable.rectangle_no_corners);
            return;
        }
        if (url == null) {
            // TODO: 2017-11-11 do this for all
            setImageResource(R.drawable.rectangle_no_corners);
            return;
        }
        Context context = getContext();
        picasso(context)
                .load(url)
                .placeholder(R.drawable.rectangle_no_corners)
                .into(this);
    }

    public void load(String url, Runnable callback) {
        if (mock) {
            setBackgroundResource(R.drawable.rectangle_no_corners);
            return;
        }
        if (url == null) {
            // TODO: 2017-11-11 do this for all
            setImageResource(R.drawable.rectangle_no_corners);
            return;
        }
        Context context = getContext();
        picasso(context)
                .load(url)
                .placeholder(R.drawable.rectangle_no_corners)
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
        if (mock) {
            setBackgroundResource(R.drawable.rectangle_no_corners);
            return;
        }
        if (url == null || thumbnailUrl == null) {
            // TODO: 2017-11-11 do this for all
            Logger.e("load: something was null");
            setImageResource(R.drawable.rectangle_no_corners);
            return;
        }
        Context context = getContext();
        Picasso.with(context)
                .load(thumbnailUrl)
                .transform(new BlurTransformation(context))
                .placeholder(R.drawable.rectangle_no_corners)
                .into(this, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context)
                                .load(url)
                                .placeholder(getDrawable())
                                .into(ImageViewLoader.this);
                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(R.drawable.error_state_car)
                                .into(ImageViewLoader.this);
                        // setOnClickListener(v -> load(thumbnailUrl, url));
                    }
                });
    }

    public void load(String url, @DimenRes int dimen) {
        if (mock) {
            return;
        }
        if (url == null) {
            return;
        }
        Context context = getContext();
        picasso(context)
                .load(url)
                .placeholder(R.drawable.rectangle)
                .transform(new RoundedCornersTransformation(context.getResources().getDimensionPixelSize(dimen), 0))
                .into(this);
    }

    public void loadNP(String url) {
        if (mock) {
            return;
        }
        if (url == null) {
            return;
        }
        Context context = getContext();
        picasso(context)
                .load(url)
                .noPlaceholder()
                .into(this);
    }

    public void loadNF(String url) {
        if (mock) {
            return;
        }
        if (url == null) {
            return;
        }
        Context context = getContext();
        picasso(context)
                .load(url)
                .noFade()
                .placeholder(R.drawable.rectangle_no_corners)
                .into(this);
    }

    public void loadNFNP(String url) {
        if (mock) {
            return;
        }
        if (url == null) {
            return;
        }
        Context context = getContext();
        picasso(context)
                .load(url)
                .noFade()
                .noPlaceholder()
                .placeholder(R.drawable.rectangle_no_corners)
                .into(this);
    }

    public void loadBlur(String url) {
        if (mock) {
            return;
        }
        if (url == null) {
            return;
        }
        Context context = getContext();
        picasso(context)
                .load(url)
                .placeholder(R.drawable.rectangle_no_corners)
                .transform(new BlurTransformation(getContext()))
                .into(this);
    }

    public void loadBlurNP(String url) {
        if (mock) {
            return;
        }
        if (url == null) {
            return;
        }
        Context context = getContext();
        picasso(context)
                .load(url)
                .noPlaceholder()
                .transform(new BlurTransformation(getContext()))
                .into(this);
    }

    public void loadForSE(final String url, final OnCompleteGlide onCompleteGlide) {
        if (mock) {
            return;
        }
        if (url == null) {
            return;
        }
        final CompletableSubject subject = CompletableSubject.create();
        subject.ambWith(Observable.just(1).take(1).delay(500, TimeUnit.MILLISECONDS).ignoreElements())
                .subscribe(() -> onCompleteGlide.onCompleteGlide());
        final Context context = getContext();
        picasso(context)
                .load(url)
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        picasso(context).load(url).noFade().noPlaceholder().into(ImageViewLoader.this);
                        subject.onComplete();
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "onError() called");
                        subject.onComplete();
                    }
                });

    }

    protected Picasso picasso(Context context) {
        Picasso picasso = Picasso.with(context);
        if (debug) {
            picasso.setLoggingEnabled(true);
            picasso.setIndicatorsEnabled(true);
        }
        return picasso;
    }

    private static final String TAG = ImageViewLoader.class.getSimpleName();

    public void load(@DrawableRes int res) {
        picasso(getContext()).load(res)
                .into(this);
    }
}
