


package com.github.pedramrn.slick.parent.ui.home.item;

import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardHeaderBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardHeader extends Item<RowCardHeaderBinding> {

    private final String title;
    private final String buttonText;
    private final View.OnClickListener onClickListener;

    public ItemCardHeader(long id, String title, String buttonText, View.OnClickListener onClickListener) {
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
        viewBinding.button.setOnClickListener(onClickListener);
    }
}
