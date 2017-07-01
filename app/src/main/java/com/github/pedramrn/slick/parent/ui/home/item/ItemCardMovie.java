


package com.github.pedramrn.slick.parent.ui.home.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardMovie extends Item<RowCardBinding> {

    private final Movie movie;

    public ItemCardMovie(long id, Movie movie) {
        super(id);
        this.movie = movie;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card;
    }

    @Override
    public void bind(RowCardBinding viewBinding, int position) {
        viewBinding.textViewTitle.setText(movie.title());
        viewBinding.imageViewPoster.load(movie.posterThumbnail());
    }

    public Movie getMovie() {
        return movie;
    }
}
