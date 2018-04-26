package com.github.pedramrn.slick.parent.ui.image;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowImageBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-06
 */

public class ItemImage extends Item<RowImageBinding> implements OnItemAction {
    private final String urlTiny;
    private final String urlMedium;
    private final String urlHigh;
    private final String urlOriginal;
    private final String urlSmall;

    public ItemImage(String url) {
        this.urlTiny = "http://image.tmdb.org/t/p/w92" + url;
        this.urlSmall = "http://image.tmdb.org/t/p/w154" + url;
        this.urlMedium = "http://image.tmdb.org/t/p/w300" + url;
        this.urlHigh = "http://image.tmdb.org/t/p/w500" + url;
        this.urlOriginal = "http://image.tmdb.org/t/p/original" + url;
    }

    @Override
    public int getLayout() {
        return R.layout.row_image;
    }

    @Override
    public void bind(RowImageBinding viewBinding, int position) {
        viewBinding.imageView.setImageDrawable(null);
        // viewBinding.imageView.loadBlurNP(urlTiny);
        viewBinding.imageView.load(urlSmall, urlHigh);
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position, @NonNull View view) {

    }
}
