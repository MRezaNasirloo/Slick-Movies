package com.github.pedramrn.slick.parent.ui.people.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.databinding.RowCardCreditBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.people.model.CastOrCrewPersonDetails;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-04
 */

public class ItemTvShowCrew extends ItemMovieCast {
    public ItemTvShowCrew(CastOrCrewPersonDetails coc) {
        super(coc);
    }

    @Override
    public void bind(RowCardCreditBinding viewBinding, int position) {
        loadPoster(viewBinding);
        renderDate(viewBinding, coc.firstAirDate());
        viewBinding.textViewTitle.setText(coc.name());
        viewBinding.textViewCharacterOrJob.setText(coc.job());
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position, @NonNull View view) {
        navigator.snackbarManager().show("Under Construction!!!");
    }
}
