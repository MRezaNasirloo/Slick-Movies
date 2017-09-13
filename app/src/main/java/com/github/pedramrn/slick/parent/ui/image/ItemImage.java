package com.github.pedramrn.slick.parent.ui.image;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowImageBinding;
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

    public ItemImage(String url) {
        this.urlTiny = "http://image.tmdb.org/t/p/w92" + url;
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
        viewBinding.imageView.loadBlurNP(urlMedium);
        viewBinding.imageView.loadNP(urlHigh);
    }

    @Override
    public void action(Controller controller, int position) {

    }
}
