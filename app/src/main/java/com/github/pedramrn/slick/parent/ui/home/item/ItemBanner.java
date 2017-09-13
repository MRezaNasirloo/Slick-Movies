package com.github.pedramrn.slick.parent.ui.home.item;

import android.support.annotation.Nullable;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBannerBinding;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.videos.ControllerVideos;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemBanner extends Item<RowBannerBinding> implements OnItemAction, ItemMovie, RemovableOnError {

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
        viewBinding.imageViewPlay.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        viewBinding.imageViewThumbnail.load(movie.thumbnailBackdrop());
        viewBinding.textViewTitleAnticipated.setBackground(null);
        viewBinding.textViewTitleAnticipated.setText(movie.title());
    }

    @Nullable
    @Override
    public MovieBasic movie() {
        return movie;
    }

    @Nullable
    @Override
    public String transitionName() {
        return null;
    }

    @Override
    public boolean removable() {
        return false;
    }

    @Override
    public void action(Controller controller, int position) {
        ControllerVideos.start(controller.getRouter(), movie, transitionName(), position);
    }
}
