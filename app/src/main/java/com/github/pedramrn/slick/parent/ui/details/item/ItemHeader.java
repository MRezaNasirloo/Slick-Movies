package com.github.pedramrn.slick.parent.ui.details.item;

import android.content.Context;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowHeaderBinding;
import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-16
 */

public class ItemHeader extends Item<RowHeaderBinding> {

    private final MovieBoxOffice movieBoxOffice;
    private final int pos;

    public ItemHeader(MovieBoxOffice movieBoxOffice, int pos) {
        this.movieBoxOffice = movieBoxOffice;
        this.pos = pos;
    }

    @Override
    public int getLayout() {
        return R.layout.row_header;
    }

    @Override
    public void bind(RowHeaderBinding viewBinding, int position) {
        Context context = viewBinding.getRoot().getContext();
        String transitionName = context.getResources().getString(R.string.transition_poster, pos);
        viewBinding.imageViewIcon.setTransitionName(transitionName);

        viewBinding.textViewTitle.setText(movieBoxOffice.name());
        // TODO: 2017-06-18 use recycler view for this
        viewBinding.textViewGenre.setText(Observable.fromIterable(movieBoxOffice.genre())
                .take(4)
                .reduce(new BiFunction<String, String, String>() {
                    @Override
                    public String apply(@NonNull String s, @NonNull String s2) throws Exception {
                        return s + " | " + s2;
                    }
                }).blockingGet());
        viewBinding.textViewRelease.setText(movieBoxOffice.released());
        viewBinding.textViewScore.setText(movieBoxOffice.scoreImdb());
        viewBinding.textViewRuntime.setText(movieBoxOffice.runtimePretty());
        viewBinding.textViewRated.setText(movieBoxOffice.certification());
        Picasso.with(context)
                .load(movieBoxOffice.posterMedium())
                .noFade()
                .into(viewBinding.imageViewIcon);

    }
}