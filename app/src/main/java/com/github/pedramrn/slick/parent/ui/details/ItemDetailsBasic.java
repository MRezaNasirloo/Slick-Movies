package com.github.pedramrn.slick.parent.ui.details;

import android.content.Context;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.SummeryBinding;
import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemDetailsBasic extends Item<SummeryBinding> {

    private final MovieBoxOffice movieBoxOffice;

    public ItemDetailsBasic(MovieBoxOffice movieBoxOffice) {
        this.movieBoxOffice = movieBoxOffice;
    }

    @Override
    public int getLayout() {
        return R.layout.summery;
    }

    @Override
    public void bind(SummeryBinding viewBinding, int position) {
        Context context = viewBinding.getRoot().getContext();
        String transitionName = context.getResources().getString(R.string.transition_poster, position);
        viewBinding.imageViewIcon.setTransitionName(transitionName);

        viewBinding.textViewTitle.setText(movieBoxOffice.name());
        viewBinding.textViewGenre.setText(movieBoxOffice.genre());
        // viewBinding.textViewPlot.setText(movieBoxOffice.plot());
        viewBinding.textViewRelease.setText(movieBoxOffice.released());
        viewBinding.textViewScore.setText(movieBoxOffice.scoreImdb());
        viewBinding.textViewRuntime.setText(movieBoxOffice.runtime());
        viewBinding.textViewRated.setText(movieBoxOffice.certification());
        Picasso.with(context)
                .load(movieBoxOffice.poster())
                .noFade()
                .into(viewBinding.imageViewIcon);

    }
}
