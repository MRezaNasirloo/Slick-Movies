package com.github.pedramrn.slick.parent.ui.home.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBannerBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-12
 */
public class ItemBannerError extends ItemBanner implements OnItemAction, RemovableOnError {
    public static final String TAG = ItemBannerError.class.getSimpleName();
    private final short code;
    private final String tag;

    public ItemBannerError(long id, String tag, short code) {
        super(id, null);
        this.code = code;
        this.tag = tag;
    }

    @Override
    public void bind(RowBannerBinding viewBinding, int position) {
        Context context = viewBinding.getRoot().getContext();
        viewBinding.layoutShimmer.stopShimmerAnimation();
        viewBinding.imageViewPlay.setImageResource(R.drawable.ic_refresh_black_24dp);
        viewBinding.imageViewThumbnail.load(ErrorHandler.resDrawable(code));
        viewBinding.textViewTitleAnticipated.setText(ErrorHandler.message(context, code));
        viewBinding.textViewTitleAnticipated.setBackground(null);
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        retryable.onRetry(tag);
    }


    @Override
    public boolean removableByTag(String tag) {
        return true;
    }
}
