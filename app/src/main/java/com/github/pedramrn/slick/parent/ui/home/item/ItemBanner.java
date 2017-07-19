package com.github.pedramrn.slick.parent.ui.home.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBannerBinding;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemBanner extends Item<RowBannerBinding> {

    private final MovieBasic movie;

    public ItemBanner(long id, MovieBasic movie) {
        super(id);
        this.movie = movie;
    }

    @Override
    public int getLayout() {
        return R.layout.row_banner;
    }

    @Override
    public void bind(RowBannerBinding viewBinding, int position) {
        viewBinding.imageViewThumbnail.load(movie.thumbnailBackdrop());
        viewBinding.imageViewThumbnail.setBackground(null);
        viewBinding.textViewTitleAnticipated.setBackground(null);
        viewBinding.textViewTitleAnticipated.setText(movie.title());
    }

    public MovieBasic getMovie() {
        return movie;
    }
}
