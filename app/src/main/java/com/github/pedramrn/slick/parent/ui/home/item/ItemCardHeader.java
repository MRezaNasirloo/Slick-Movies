


package com.github.pedramrn.slick.parent.ui.home.item;

import android.util.Log;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardHeaderBinding;
import com.jakewharton.rxbinding2.view.RxView;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import io.reactivex.Observer;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardHeader extends Item<RowCardHeaderBinding> {

    private final String title;
    private final String buttonText;
    private final Observer onClickListener;

    public ItemCardHeader(long id, String title, String buttonText, Observer onClickListener) {
        super(id);
        this.title = title;
        this.buttonText = buttonText;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_header;
    }

    @Override
    public void bind(RowCardHeaderBinding viewBinding, int position) {
        viewBinding.textViewTitle.setText(title);
        viewBinding.button.setText(buttonText);
        RxView.clicks(viewBinding.button).subscribe(onClickListener);

    }

    @Override
    public void unbind(ViewHolder<RowCardHeaderBinding> holder) {
        Log.d(TAG, "unbind() called");
        holder.binding.button.setOnClickListener(null);
        super.unbind(holder);
    }

    private static final String TAG = ItemCardHeader.class.getSimpleName();
}
