package com.github.pedramrn.slick.parent.ui.boxoffice.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowBoxOfficeErrorBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-16
 */

public class ItemBoxOfficeError extends Item<RowBoxOfficeErrorBinding> implements RemovableOnError, OnItemAction {


    private final short code;

    public ItemBoxOfficeError(long id, short code) {
        super(id);
        this.code = code;
    }

    @Override
    public int getLayout() {
        return R.layout.row_box_office_error;
    }

    @Override
    public void bind(RowBoxOfficeErrorBinding holder, int position) {
        holder.imageView.load(ErrorHandler.resDrawable(code));
    }


    @Override
    public boolean removableByTag(String tag) {
        return true;
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        retryable.onRetry(MovieSmall.BOX_OFFICE);
    }
}
