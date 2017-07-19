package com.github.pedramrn.slick.parent.ui.home.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBannerBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemBanner extends Item<RowBannerBinding> {

    private final String thumbnail;
    private final String title;

    public ItemBanner(long id, String thumbnail, String title) {
        super(id);
        this.thumbnail = thumbnail;
        this.title = title;
    }

    @Override
    public int getLayout() {
        return R.layout.row_banner;
    }

    @Override
    public void bind(RowBannerBinding viewBinding, int position) {
        viewBinding.imageViewThumbnail.load(thumbnail);
        viewBinding.imageViewThumbnail.setBackground(null);
        viewBinding.textViewTitleAnticipated.setBackground(null);
        viewBinding.textViewTitleAnticipated.setText(title);
    }
}
