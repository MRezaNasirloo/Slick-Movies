package com.github.pedramrn.slick.parent.ui.details.item;

import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCommentNestedBinding;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-24
 */

public class ItemCommentProgressive extends ItemComment {

    public ItemCommentProgressive(long id, String tag) {
        super(id, null, tag);
    }

    @Override
    public void bind(RowCommentNestedBinding viewBinding, int position) {
        viewBinding.textViewComment.setText("         \n       ");
        viewBinding.textViewUserNameDate.setText("                ");
        viewBinding.textViewReadMore.setVisibility(View.INVISIBLE);
        viewBinding.textViewComment.setBackgroundResource(R.drawable.line);
        viewBinding.textViewUserNameDate.setBackgroundResource(R.drawable.line);
    }
}
