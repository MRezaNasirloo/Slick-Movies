


package com.github.pedramrn.slick.parent.ui.home.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.xwray.groupie.ViewHolder;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardMovieProgressive extends ItemCardMovie {

    public ItemCardMovieProgressive(long id, Movie movie) {
        super(id, movie);
    }

    public ItemCardMovieProgressive(long id) {
        super(id, null);
    }

    @Override
    public int getLayout() {
        return R.layout.row_card;
    }

    @Override
    public void bind(RowCardBinding viewBinding, int position) {
        if (getMovie() != null) {
            super.bind(viewBinding, position);
            return;
        }
        viewBinding.textViewTitle.setText("         ");
        viewBinding.textViewTitle.setBackgroundResource(R.drawable.line);
    }

    @Override
    public void unbind(ViewHolder<RowCardBinding> holder) {
        super.unbind(holder);
        holder.binding.textViewTitle.setBackground(null);
    }
}
