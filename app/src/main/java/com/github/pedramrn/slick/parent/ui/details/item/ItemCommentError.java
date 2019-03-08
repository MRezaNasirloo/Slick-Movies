package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCommentErrorBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.databinding.BindableItem;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-15
 */
public class ItemCommentError extends BindableItem<RowCommentErrorBinding> implements OnItemAction, RemovableOnError {

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
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        retryable.onRetry(tag);
    }

    @Override
    public boolean removableByTag(String tag) {
        return true;
    }
}
