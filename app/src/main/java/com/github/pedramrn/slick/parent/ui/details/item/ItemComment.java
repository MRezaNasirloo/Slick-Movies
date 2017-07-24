package com.github.pedramrn.slick.parent.ui.details.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCommentBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Comment;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-24
 */

public class ItemComment extends Item<RowCommentBinding> {

    private final Comment comment;

    public ItemComment(long id, Comment comment, String tag) {
        super(id);
        this.comment = comment;
    }

    @Override
    public int getLayout() {
        return R.layout.row_comment;
    }

    @Override
    public void bind(final RowCommentBinding viewBinding, int position) {
        viewBinding.textViewComment.setText(comment.comment());
        String name = comment.user().name();
        String text = name != null && !name.isEmpty() ? name : comment.user().username();
        text = "Read more \n\n" + text;
        viewBinding.textViewUserNameDate.setText(text);
        viewBinding.textViewReplies.setText(String.valueOf(comment.replies()));
        viewBinding.textViewLikes.setText(String.valueOf(comment.likes()));
        // viewBinding.textViewReadMore.setVisibility(View.INVISIBLE);// TODO: 2017-07-24 if needed
        viewBinding.textViewComment.setBackground(null);
        viewBinding.textViewUserNameDate.setBackground(null);
        viewBinding.textViewReplies.setBackground(null);
        viewBinding.textViewLikes.setBackground(null);
    }
}