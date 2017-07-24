package com.github.pedramrn.slick.parent.ui.details.item;

import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCommentNestedBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Comment;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-24
 */

public class ItemComment extends Item<RowCommentNestedBinding> {

    private final Comment comment;

    public ItemComment(long id, Comment comment, String tag) {
        super(id);
        this.comment = comment;
    }

    @Override
    public int getLayout() {
        return R.layout.row_comment_nested;
    }

    @Override
    public void bind(final RowCommentNestedBinding viewBinding, int position) {
        viewBinding.textViewComment.setText(comment.comment());
        String name = comment.user().name();
        viewBinding.textViewUserNameDate.setText(name != null && !name.isEmpty() ? name : comment.user().username());
        viewBinding.textViewReplies.setText(String.valueOf(comment.replies()));
        viewBinding.textViewLikes.setText(String.valueOf(comment.likes()));
        viewBinding.textViewReadMore.setVisibility(View.INVISIBLE);// TODO: 2017-07-24 if needed
        viewBinding.textViewComment.setBackground(null);
        viewBinding.textViewUserNameDate.setBackground(null);
        viewBinding.textViewReplies.setBackground(null);
        viewBinding.textViewLikes.setBackground(null);
    }
}
