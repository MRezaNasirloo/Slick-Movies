package com.github.pedramrn.slick.parent.ui.boxoffice.item;

import com.android.databinding.library.baseAdapters.BR;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBoxOfficeBinding;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-16
 */

public class ItemBoxOffice extends Item<RowBoxOfficeBinding> {

    private final MovieBasic movie;
    private final String transitionName;

    public ItemBoxOffice(long id, MovieBasic movie, String transitionName) {
        super(id);
        this.movie = movie;
        this.transitionName = transitionName;
    }

    @Override
    public int getLayout() {
        return R.layout.row_box_office;
    }

    @Override
    public void bind(RowBoxOfficeBinding holder, int position) {
        holder.setVariable(BR.vm, movie);
        holder.setRank(movie.rank(position));
        holder.textViewTitle.setSelected(true);
        holder.imageView.load(movie.thumbnailTinyPoster(), movie.thumbnailPoster());
        holder.imageView.setTransitionName(transitionName);
    }
}
