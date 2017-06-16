package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCastBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class CastItem extends Item<RowCastBinding> {

    private final Cast cast;

    public CastItem(Cast cast) {
        this.cast = cast;
    }

    @Override
    public int getLayout() {
        return R.layout.row_cast;
    }

    @Override
    public void bind(RowCastBinding viewBinding, int position) {
        viewBinding.imageViewProfile.load(cast.profileIcon());
        viewBinding.textViewName.setText(cast.name());
        viewBinding.textViewCharacter.setText(cast.character());
    }

    public Cast getCast() {
        return cast;
    }
}
