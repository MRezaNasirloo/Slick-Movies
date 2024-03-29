package com.github.pedramrn.slick.parent.ui.details.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCommentBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-24
 */

public class ItemCommentProgressive extends ItemComment {

    public ItemCommentProgressive(long id, String tag) {
        super(id, null, tag);
    }

    @Override
    public void bind(RowCommentBinding viewBinding, int position) {
        viewBinding.textViewComment.setText("         \n       ");
        viewBinding.textViewUserNameDate.setText("                ");
        // viewBinding.textViewReadMore.setVisibility(View.INVISIBLE);
        viewBinding.textViewComment.setBackgroundResource(R.drawable.line);
        viewBinding.textViewUserNameDate.setBackgroundResource(R.drawable.line);
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        //no-op
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public boolean removableByTag(String tag) {
        return true;
    }
}
