package com.github.pedramrn.slick.parent.ui.details.item;

import com.github.pedramrn.slick.parent.databinding.RowBackdropBinding;
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

    private static final String TAG = ItemBackdrop.class.getSimpleName();
}
