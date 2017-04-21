package com.github.pedramrn.slick.parent.ui.android;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public abstract class ImageLoader {

    public abstract ImageLoader with(Context context);

    public abstract ImageLoader load(String url);

    public abstract void into(ImageView imageView);

}
