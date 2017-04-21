package com.github.pedramrn.slick.parent.ui.android;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

class ImageLoaderPicassoImpl implements ImageLoader {

    private RequestCreator requestCreator;
    private Picasso picasso;


    @Override
    public ImageLoader with(Context context) {
        picasso = Picasso.with(context);
        return this;
    }

    @Override
    public ImageLoader load(String url) {
        requestCreator = picasso.load(url);
        return this;
    }

    @Override
    public void into(ImageView imageView) {
        requestCreator.into(imageView);
    }
}
