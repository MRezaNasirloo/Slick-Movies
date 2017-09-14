package com.github.pedramrn.slick.parent.ui.home.item;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardErrorBinding;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-09
 */
public class ItemError extends Item<RowCardErrorBinding> implements OnItemAction, RemovableOnError {

    private final String tag;
    private final String message;
    private final Throwable throwable;

    public ItemError(long id, String tag, String message) {
        super(id);
        this.tag = tag;
        this.message = message;
        throwable = null;
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
    public void action(Controller controller, int position) {
        if (controller instanceof Retryable)
            ((Retryable) controller).onRetry(tag);
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
