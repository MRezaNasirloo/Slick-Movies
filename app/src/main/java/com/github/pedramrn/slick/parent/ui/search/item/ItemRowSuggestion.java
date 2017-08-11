package com.github.pedramrn.slick.parent.ui.search.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowSearchSuggestionBinding;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class ItemRowSuggestion extends Item<RowSearchSuggestionBinding> {
    @Override
    public int getLayout() {
        return R.layout.row_search_suggestion;
    }

    @Override
    public void bind(RowSearchSuggestionBinding viewBinding, int position) {
        viewBinding.imageViewIcon.setImageResource(R.drawable.ic_search_black_24dp);
        viewBinding.textViewTitle.setText("Movie Name");
    }
}
