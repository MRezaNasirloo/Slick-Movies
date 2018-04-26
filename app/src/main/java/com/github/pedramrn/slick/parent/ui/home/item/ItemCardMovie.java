


package com.github.pedramrn.slick.parent.ui.home.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

import java.util.Locale;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardMovie extends Item<RowCardBinding> implements OnItemAction, ItemMovie, RemovableOnError {

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
        viewBinding.layoutShimmer.stopShimmerAnimation();
        viewBinding.imageViewPoster.setImageResource(R.drawable.rectangle_no_corners);
        viewBinding.textViewTitle.setText(movie.year() != null ? String.format(
                Locale.getDefault(),
                "%s  (%d)",
                movie.title(),
                movie.year()
        ) : movie.title());
        viewBinding.textViewTitle.setBackground(null);
        viewBinding.imageViewPoster.setBackground(null);
        viewBinding.imageViewPoster.setTransitionName(transitionName);
        viewBinding.imageViewPoster.load(movie.thumbnailTinyPoster(), movie.thumbnailPoster());
    }

    @Override
    @Nullable
    public String transitionName() {
        return transitionName;
    }

    @Override
    @Nullable
    public MovieBasic movie() {
        return movie;
    }

    @Override
    public boolean removableByTag(String tag) {
        return false;
    }

    private static final String TAG = ItemCardMovie.class.getSimpleName();
    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position, @NonNull View view) {
        navigator.navigateTo(ControllerDetails.newInstance(movie, transitionName), view.findViewById(R.id.imageView_poster),
                transitionName);
    }
}
