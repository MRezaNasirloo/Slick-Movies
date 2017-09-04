package com.github.pedramrn.slick.parent.ui.people.item;

import com.github.pedramrn.slick.parent.databinding.RowCardCreditBinding;
import com.github.pedramrn.slick.parent.ui.people.model.CastOrCrewPersonDetails;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-04
 */

public class ItemMovieCrew extends ItemMovieCast {
    public ItemMovieCrew(CastOrCrewPersonDetails coc) {
        super(coc);
    }

    @Override
    public void bind(RowCardCreditBinding viewBinding, int position) {
        loadPoster(viewBinding);
        renderDate(viewBinding, coc.releaseDate());
        viewBinding.textViewTitle.setText(coc.title());
        viewBinding.textViewCharacterOrJob.setText(coc.job());
    }
}
