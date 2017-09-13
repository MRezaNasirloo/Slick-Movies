


package com.github.pedramrn.slick.parent.ui.home.item;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardBinding;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

public class ItemCardMovieProgressive extends ItemCardMovie {

    public ItemCardMovieProgressive(long id, Movie movie) {
        super(id, movie, null);
    }

    public ItemCardMovieProgressive(long id) {
        super(id, null, null);
    }

    @Override
    public void bind(RowCardBinding viewBinding, int position) {
        if (movie() != null) {
            super.bind(viewBinding, position);
            return;
        }
        viewBinding.textViewTitle.setText("         ");
        viewBinding.textViewTitle.setBackgroundResource(R.drawable.line);
        viewBinding.imageViewPoster.setImageResource(R.drawable.rectangle_no_corners);
    }

    @Override
    public boolean removable() {
        return true;
    }

    @Override
    public void action(Controller controller, int position) {
        //no-op
    }
}
