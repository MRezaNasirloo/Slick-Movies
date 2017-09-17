package com.github.pedramrn.slick.parent.ui.search.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowSearchSuggestionBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.item.ItemMovie;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class ItemRowSuggestion extends Item<RowSearchSuggestionBinding> implements ItemMovie, OnItemAction {
    private final MovieBasic movie;
    private final String transitionName;

    public ItemRowSuggestion(MovieBasic movie) {
        super(movie.id());
        this.movie = movie;
        this.transitionName = ItemRowSuggestion.class.getSimpleName() + movie.id();
    }

    @Override
    public int getLayout() {
        return R.layout.row_search_suggestion;
    }

    @Override
    public void bind(RowSearchSuggestionBinding viewBinding, int position) {
        viewBinding.imageViewIcon.load(movie.thumbnailTinyPoster());
        viewBinding.imageViewIcon.setTransitionName(transitionName);
        String releaseDate = movie.releaseDate();
        viewBinding.textViewTitle.setText(String.format("%s (%s)", movie.title(), releaseDate != null ? releaseDate.split("-")[0] : "n/a"));
    }

    @Nullable
    @Override
    public MovieBasic movie() {
        return movie;
    }

    @Nullable
    @Override
    public String transitionName() {
        return transitionName;
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        ControllerDetails.start(navigator.getRouter(), movie, transitionName);
    }
}
