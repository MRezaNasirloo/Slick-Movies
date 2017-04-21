package com.github.pedramrn.slick.parent.ui.android;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public interface ImageLoader {

    ImageLoader with(Context context);

    ImageLoader load(String url);

    void into(ImageView imageView);

}
