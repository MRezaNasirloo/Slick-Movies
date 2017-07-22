package com.github.pedramrn.slick.parent.ui.details.item;

import com.github.pedramrn.slick.parent.databinding.RowCastHorizontalBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemCast extends ItemCastProgressive {

    private final Cast cast;

    public ItemCast(int id, Cast cast) {
        super(id);
        this.cast = cast;
    }

    @Override
    public void bind(RowCastHorizontalBinding viewBinding, int position) {
        viewBinding.imageViewProfile.load(cast.profileIcon());
        viewBinding.textViewName.setText(cast.name());
        viewBinding.textViewCharacter.setText(cast.character());
    }

    public Cast getCast() {
        return cast;
    }
}
