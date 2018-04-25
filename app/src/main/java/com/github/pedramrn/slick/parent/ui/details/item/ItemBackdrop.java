package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.databinding.RowBackdropBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-17
 */

public class ItemBackdrop extends ItemBackdropProgressive {

    private final Backdrop backdrop;

    public ItemBackdrop(int id, Backdrop backdrop) {
        super(id);
        this.backdrop = backdrop;
    }


    @Override
    public void bind(RowBackdropBinding viewBinding, int position) {
        viewBinding.imageViewBackdrop.load(backdrop.backdropThumbnail());
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        /*ControllerImage.start(
                navigator.getRouter(),
                backdrop.movieTitle(),
                ((ArrayList<String>) backdrop.allBackdrops()),
                position
        );*/
    }

    private static final String TAG = ItemBackdrop.class.getSimpleName();
}
