package com.github.pedramrn.slick.parent.ui.home.item;

import android.util.Log;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBannerBinding;
import com.github.pedramrn.slick.parent.ui.home.ControllerHome;
import com.github.pedramrn.slick.parent.ui.home.RouterProvider;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-12
 */
public class ItemBannerError extends ItemBanner implements OnItemAction, RemovableOnError {
    public static final String TAG = ItemBannerError.class.getSimpleName();
    private final Throwable error;

    public ItemBannerError(long id, Throwable error) {
        super(id, null);
        this.error = error;
    }

    @Override
    public void bind(RowBannerBinding viewBinding, int position) {
        viewBinding.imageViewPlay.setImageResource(R.drawable.ic_refresh_black_24dp);
        viewBinding.imageViewThumbnail.setImageResource(R.drawable.rectangle_no_corners);
        viewBinding.textViewTitleAnticipated.setText(error.getMessage());
        viewBinding.textViewTitleAnticipated.setBackground(null);
    }

    @Override
    public void action(RouterProvider router) {
        Log.d(TAG, "action: called");
        Controller controller = router.get().getControllerWithTag("ControllerHome");
        if (controller != null) {
            ((ControllerHome) controller).onClickRetryUpcoming(this);
        }
    }

    @Override
    public boolean removable() {
        return true;
    }
}
