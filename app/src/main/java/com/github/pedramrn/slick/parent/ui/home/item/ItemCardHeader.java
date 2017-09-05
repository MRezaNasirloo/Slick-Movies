


package com.github.pedramrn.slick.parent.ui.home.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardHeaderBinding;
import com.jakewharton.rxbinding2.view.RxView;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardHeader extends Item<RowCardHeaderBinding> {

    private final String title;
    private final String buttonText;
    private final Consumer<Object> onClickListener;
    private Disposable disposable;

    public ItemCardHeader(long id, @NonNull String title, @Nullable String buttonText, @Nullable Consumer<Object> onClickListener) {
        super(id);
        this.title = title;
        this.buttonText = buttonText;
        this.onClickListener = onClickListener;
    }

    public ItemCardHeader(int id, @NonNull String title) {
        super(id);
        this.title = title;
        this.onClickListener = null;
        this.buttonText = null;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_header;
    }

    @Override
    public void bind(RowCardHeaderBinding viewBinding, int position) {
        viewBinding.textViewTitle.setText(title);
        viewBinding.button.setText(buttonText);
        if (onClickListener != null) {
            disposable = RxView.clicks(viewBinding.button).subscribe(onClickListener);
        }
        if (buttonText == null) {
            viewBinding.button.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void unbind(ViewHolder<RowCardHeaderBinding> holder) {
        Log.d(TAG, "unbind() called");
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        holder.binding.button.setOnClickListener(null);
        super.unbind(holder);
    }

    private static final String TAG = ItemCardHeader.class.getSimpleName();
}
