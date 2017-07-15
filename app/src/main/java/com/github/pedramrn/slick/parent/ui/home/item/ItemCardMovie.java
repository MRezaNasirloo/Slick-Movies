


package com.github.pedramrn.slick.parent.ui.home.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardBinding;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardMovie extends Item<RowCardBinding> {

    private final MovieBasic movie;
    private final String transitionName;

    public ItemCardMovie(long id, MovieBasic movie, String tag) {
        super(id);
        this.movie = movie;
        this.transitionName = tag + "_" + id;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card;
    }

    @Override
    public void bind(RowCardBinding viewBinding, int position) {
        viewBinding.textViewTitle.setText(movie.title());
        viewBinding.textViewTitle.setBackground(null);
        viewBinding.imageViewPoster.setBackground(null);
        viewBinding.imageViewPoster.setTransitionName(transitionName);
        viewBinding.imageViewPoster.load(movie.posterThumbnail());
    }

    public String getTransitionName() {
        return transitionName;
    }

    public MovieBasic getMovie() {
        return movie;
    }
}
