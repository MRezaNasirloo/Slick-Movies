package com.github.pedramrn.slick.parent.ui.details.item;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.databinding.RowBackdropBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.image.ControllerImage;

import java.util.ArrayList;

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
    public void action(Controller controller, int position) {
        ControllerImage.start(
                controller.getRouter(),
                backdrop.movieTitle(),
                ((ArrayList<String>) backdrop.allBackdrops()),
                position
        );
    }

    private static final String TAG = ItemBackdrop.class.getSimpleName();
}
