package com.github.pedramrn.slick.parent.ui.home.item;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBannerBinding;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-12
 */
public class ItemBannerError extends ItemBanner implements OnItemAction, RemovableOnError {
    public static final String TAG = ItemBannerError.class.getSimpleName();
    private final Throwable error;
    private final String tag;

    public ItemBannerError(long id, String tag, Throwable error) {
        super(id, null);
        this.error = error;
        this.tag = tag;
    }

    @Override
    public void bind(RowBannerBinding viewBinding, int position) {
        viewBinding.imageViewPlay.setImageResource(R.drawable.ic_refresh_black_24dp);
        viewBinding.imageViewThumbnail.setImageResource(R.drawable.rectangle_no_corners);
        viewBinding.textViewTitleAnticipated.setText(error.getMessage());
        viewBinding.textViewTitleAnticipated.setBackground(null);
    }

    @Override
    public void action(Controller controller, int position) {
        if (controller instanceof Retryable)
            ((Retryable) controller).onRetry(tag);
    }


    @Override
    public boolean removable() {
        return true;
    }
}
