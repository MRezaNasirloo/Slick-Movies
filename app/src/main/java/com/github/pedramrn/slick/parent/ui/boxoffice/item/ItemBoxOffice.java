package com.github.pedramrn.slick.parent.ui.boxoffice.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBoxOfficeBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-16
 */

public class ItemBoxOffice extends Item<RowBoxOfficeBinding> implements RemovableOnError, OnItemAction {

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
        holder.title.setText(movie.title());
        holder.rank.setText(movie.rank(position));
        holder.poster.load(movie.thumbnailTinyPoster(), movie.thumbnailPoster());
        holder.poster.setTransitionName(transitionName);
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        System.out.println("ItemBoxOffice.action");
        ControllerDetails.start(navigator.getRouter(), movie, transitionName);
    }

    @Override
    public boolean isClickable() {
        System.out.println("ItemBoxOffice.isClickable");
        return true;
    }

    @Override
    public boolean removableByTag(String tag) {
        return false;
    }
}
