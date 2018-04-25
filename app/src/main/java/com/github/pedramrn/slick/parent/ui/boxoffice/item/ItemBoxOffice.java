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

import java.util.Locale;

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
        if (movie.revenue() != null) holder.revenue.setText(String.format("%s $", withSuffix(movie.revenue())));
        holder.title.setText(movie.title());
        holder.rank.setText(movie.rank(position));
        holder.poster.load(movie.thumbnailTinyPoster(), movie.thumbnailPoster());
        holder.poster.setTransitionName(transitionName);
    }

    private static String withSuffix(@Nullable Long amount) {
        if (amount == null) return "n/a";
        if (amount < 1000) return "" + amount;
        int exp = (int) (Math.log(amount) / Math.log(1000));
        return String.format(Locale.ENGLISH, "%.1f %c", amount / Math.pow(1000, exp), "kmBTPE".charAt(exp - 1));
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        ControllerDetails.newInstance(movie, transitionName);
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public boolean removableByTag(String tag) {
        return false;
    }
}
