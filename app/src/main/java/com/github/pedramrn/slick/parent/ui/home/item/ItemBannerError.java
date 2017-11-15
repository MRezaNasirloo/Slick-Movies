package com.github.pedramrn.slick.parent.ui.home.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBannerBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-12
 */
public class ItemBannerError extends ItemBanner implements OnItemAction, RemovableOnError {
    public static final String TAG = ItemBannerError.class.getSimpleName();
    private final String message;
    private final String tag;

    public ItemBannerError(long id, String tag, String message) {
        super(id, null);
        this.message = message;
        this.tag = tag;
    }

    @Override
    public void bind(RowBannerBinding viewBinding, int position) {
        viewBinding.imageViewPlay.setImageResource(R.drawable.ic_refresh_black_24dp);
        viewBinding.imageViewThumbnail.setImageResource(R.drawable.rectangle_no_corners);
        viewBinding.textViewTitleAnticipated.setText(message);
        viewBinding.textViewTitleAnticipated.setBackground(null);
    }

    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        if (navigator instanceof Retryable)
            ((Retryable) navigator).onRetry(tag);
    }


    @Override
    public boolean removableByTag(String tag) {
        return true;
    }
}
