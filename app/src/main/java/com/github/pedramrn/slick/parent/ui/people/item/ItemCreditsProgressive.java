package com.github.pedramrn.slick.parent.ui.people.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardCreditBinding;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-04
 */

public class ItemCreditsProgressive extends ItemMovieCast {
    public ItemCreditsProgressive() {
        super(null);
    }

    @Override
    public void bind(RowCardCreditBinding viewBinding, int position) {
        viewBinding.textViewTitle.setText("         ");
        viewBinding.textViewCharacterOrJob.setText("    ");
        viewBinding.textViewTitle.setBackgroundResource(R.drawable.line);
        viewBinding.textViewCharacterOrJob.setBackgroundResource(R.drawable.line);
        viewBinding.textViewEpisodeAndYear.setBackgroundResource(R.drawable.line);
        viewBinding.imageViewPoster.setImageResource(R.drawable.rectangle_no_corners);
    }
}
