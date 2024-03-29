package com.github.pedramrn.slick.parent.ui.home.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardErrorBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-09
 */
public class ItemError extends Item<RowCardErrorBinding> implements OnItemAction, RemovableOnError {

    private final String tag;
    private final short code;
    private final Throwable throwable;

    public ItemError(long id, String tag, short code) {
        super(id);
        this.tag = tag;
        this.code = code;
        throwable = null;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_error;
    }

    @Override
    public void bind(RowCardErrorBinding viewBinding, int position) {
        viewBinding.imageViewPoster.load(ErrorHandler.resDrawable(code));
        viewBinding.textViewTitle.setText(ErrorHandler.message(viewBinding.getRoot().getContext(), code));
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        retryable.onRetry(tag);
    }

    public Throwable throwable() {
        return throwable;
    }

    public static final String TAG = ItemError.class.getSimpleName();

    @Override
    public boolean removableByTag(String tag) {
        return true;
    }
}
