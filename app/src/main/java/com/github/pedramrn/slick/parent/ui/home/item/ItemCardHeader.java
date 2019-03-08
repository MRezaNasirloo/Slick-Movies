


package com.github.pedramrn.slick.parent.ui.home.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardHeaderBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.jakewharton.rxbinding2.view.RxView;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.databinding.ViewHolder;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardHeader extends BindableItem<RowCardHeaderBinding> implements OnItemAction {

    private final String title;
    private final String buttonText;
    private Consumer<Object> onClickListener;
    private Disposable disposable;

    public ItemCardHeader(long id, @NonNull String title, @Nullable String buttonText) {
        super(id);
        this.title = title;
        this.buttonText = buttonText;
    }

    public ItemCardHeader(int id, @NonNull String title) {
        super(id);
        this.title = title;
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
            disposable = RxView.clicks(viewBinding.button).throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(onClickListener);
        }
        if (buttonText == null || buttonText.trim().isEmpty()) {
            viewBinding.button.setVisibility(View.GONE);
        } else {
            viewBinding.button.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void unbind(@NonNull ViewHolder<RowCardHeaderBinding> holder) {
        Log.d(TAG, "unbind() called " + title);
        UtilsRx.dispose(disposable);
        holder.binding.button.setOnClickListener(null);
        super.unbind(holder);
    }

    private static final String TAG = ItemCardHeader.class.getSimpleName();

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        //no-op
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    public void setOnClickListener(Consumer<Object> onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void onDestroyView() {
        UtilsRx.dispose(disposable);
    }
}
