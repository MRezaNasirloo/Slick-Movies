package com.github.pedramrn.slick.parent.ui.home.item;

import android.util.Log;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardErrorBinding;
import com.github.pedramrn.slick.parent.ui.home.ControllerHome;
import com.github.pedramrn.slick.parent.ui.home.RouterProvider;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-09
 */
public class ItemError extends Item<RowCardErrorBinding> implements OnItemAction, RemovableOnError {

    private final String message;
    private final Throwable throwable;

    public ItemError(String message) {
        this.message = message;
        throwable = null;
    }

    public ItemError(long id, String message) {
        super(id);
        this.message = message;
        throwable = null;
    }

    public ItemError(Throwable throwable) {
        message = throwable.getMessage();
        this.throwable = throwable;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_error;
    }

    @Override
    public void bind(RowCardErrorBinding viewBinding, int position) {
        viewBinding.textViewTitle.setText(message);
    }

    @Override
    public void action(RouterProvider router) {
        Log.d(TAG, "action: called");
        Controller controller = router.get().getControllerWithTag("ControllerHome");
        if (controller != null) {
            ((ControllerHome) controller).onClickRetryTrending(this);
        }
    }

    public Throwable throwable() {
        return throwable;
    }

    public static final String TAG = ItemError.class.getSimpleName();

    @Override
    public boolean removable() {
        return true;
    }
}
