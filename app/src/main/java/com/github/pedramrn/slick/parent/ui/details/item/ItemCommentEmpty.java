package com.github.pedramrn.slick.parent.ui.details.item;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCommentEmptyBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Comment;
import com.github.pedramrn.slick.parent.ui.home.item.RemovableOnError;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-15
 */
public class ItemCommentEmpty extends Item<RowCommentEmptyBinding> implements OnItemAction, RemovableOnError {

    private final String tag;

    public ItemCommentEmpty(long id, String tag) {
        super(id);
        this.tag = tag;
    }

    @Override
    public int getLayout() {
        return R.layout.row_comment_empty;
    }

    @Override
    public void bind(RowCommentEmptyBinding viewBinding, int position) {

    }


    @Override
    public void action(Controller controller, int position) {
        //no-op
    }

    @Override
    public boolean removable() {
        return false;
    }
}
