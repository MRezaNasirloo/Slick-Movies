package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCommentErrorBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-15
 */
public class ItemCommentError extends Item<RowCommentErrorBinding> implements OnItemAction, RemovableOnError {

    private final String message;
    private final String tag;

    public ItemCommentError(long id, String message, String tag) {
        super(id);
        this.message = message;
        this.tag = tag;
    }

    @Override
    public int getLayout() {
        return R.layout.row_comment_error;
    }

    @Override
    public void bind(RowCommentErrorBinding viewBinding, int position) {
        viewBinding.textView.setText(message);
    }


    @Override
    public void action(@NonNull Navigator navigator, @Nullable Object payload, int position) {
        if (navigator instanceof Retryable)
            ((Retryable) navigator).onRetry(tag);
    }

    @Override
    public boolean removable() {
        return true;
    }
}