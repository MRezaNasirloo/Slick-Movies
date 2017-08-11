package com.github.pedramrn.slick.parent.ui.search.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowSearchSuggestionBinding;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class ItemRowSuggestion extends Item<RowSearchSuggestionBinding> {
    private final MovieBasic movie;

    public ItemRowSuggestion(MovieBasic movie) {
        super(movie.id());
        this.movie = movie;
    }

    @Override
    public int getLayout() {
        return R.layout.row_search_suggestion;
    }

    @Override
    public void bind(RowSearchSuggestionBinding viewBinding, int position) {
        viewBinding.imageViewIcon.load(movie.thumbnailTinyPoster());
        String releaseDate = movie.releaseDate();
        viewBinding.textViewTitle.setText(String.format("%s (%s)", movie.title(), releaseDate != null ? releaseDate.split("-")[0] : "n/a"));
    }
}
