package com.github.pedramrn.slick.parent.ui.people.item;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardCreditBinding;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-04
 */

public class ItemCreditsProgressive extends Item<RowCardCreditBinding> implements OnItemAction {
    public ItemCreditsProgressive() {
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_credit;
    }

    @Override
    public void bind(RowCardCreditBinding viewBinding, int position) {
        viewBinding.textViewTitle.setText("         ");
        viewBinding.textViewCharacterOrJob.setText("       ");
        viewBinding.textViewTitle.setBackgroundResource(R.drawable.line);
        viewBinding.textViewCharacterOrJob.setBackgroundResource(R.drawable.line);
        viewBinding.textViewEpisodeAndYear.setBackgroundResource(R.drawable.line);
        viewBinding.imageViewPoster.setImageResource(R.drawable.rectangle_no_corners);
    }

    @Override
    public void action(Controller controller, int position) {
        //no-op
    }
}
