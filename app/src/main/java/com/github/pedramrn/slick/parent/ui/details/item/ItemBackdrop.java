package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.databinding.RowBackdropBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.image.ControllerImage;

import java.util.ArrayList;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-17
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
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position, @NonNull View view) {
        navigator.navigateTo(ControllerImage.newInstance(
                backdrop.movieTitle(),
                ((ArrayList<String>) backdrop.allBackdrops()),
                position
        ));
    }

    private static final String TAG = ItemBackdrop.class.getSimpleName();
}
