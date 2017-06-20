package com.github.pedramrn.slick.parent.ui.home.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowAnticipatedBinding;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemAnticipated extends Item<RowAnticipatedBinding> {

    public ItemAnticipated(long id, Video video) {
        super(id);
    }

    @Override
    public int getLayout() {
        return R.layout.row_anticipated;
    }

    @Override
    public void bind(RowAnticipatedBinding viewBinding, int position) {
        viewBinding.imageViewThumbnail.load("url");
    }
}
