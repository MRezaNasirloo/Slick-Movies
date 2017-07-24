package com.github.pedramrn.slick.parent.ui.details.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCommentBinding;

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
        // viewBinding.textViewReplies.setText("   ");
        // viewBinding.textViewLikes.setText("   ");
        viewBinding.textViewComment.setBackgroundResource(R.drawable.line);
        viewBinding.textViewUserNameDate.setBackgroundResource(R.drawable.line);
        // viewBinding.textViewReplies.setBackgroundResource(R.drawable.line);
        // viewBinding.textViewLikes.setBackgroundResource(R.drawable.line);
    }
}
