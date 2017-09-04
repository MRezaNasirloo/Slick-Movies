


package com.github.pedramrn.slick.parent.ui.home.item;

import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardBinding;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.xwray.groupie.Item;

import java.text.ParseException;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardMovie extends Item<RowCardBinding> implements ItemMovie {

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
        SpannableStringBuilder builder = new SpannableStringBuilder(movie.title());
        try {
            builder.append("  (" + DateUtils.formatyyyy(DateUtils.toDate(movie.releaseDate())) + ")",
                    new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewBinding.textViewTitle.setText(builder);
        } catch (ParseException | NullPointerException e) {
            viewBinding.textViewTitle.setText(builder);
        }
        viewBinding.textViewTitle.setBackground(null);
        viewBinding.imageViewPoster.setBackground(null);
        viewBinding.imageViewPoster.setTransitionName(transitionName);
        viewBinding.imageViewPoster.loadBlur(movie.thumbnailTinyPoster());
        viewBinding.imageViewPoster.load(movie.thumbnailPoster());

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
}
