package com.github.pedramrn.slick.parent.ui.people.item;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardCreditBinding;
import com.github.pedramrn.slick.parent.ui.people.model.CastOrCrewPersonDetails;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.xwray.groupie.Item;

import java.text.ParseException;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

public class ItemMovieCast extends Item<RowCardCreditBinding> {

    protected final CastOrCrewPersonDetails coc;

    public ItemMovieCast(CastOrCrewPersonDetails coc) {
        this.coc = coc;
    }

    @Override
    public int getLayout() {
        return R.layout.row_card_credit;
    }

    @Override
    public void bind(RowCardCreditBinding viewBinding, int position) {
        loadPoster(viewBinding);
        renderDate(viewBinding, coc.releaseDate());
        viewBinding.textViewTitle.setText(coc.title());
        viewBinding.textViewCharacterOrJob.setText(coc.character());
    }

    private void clearBackgrounds(RowCardCreditBinding viewBinding) {
        viewBinding.textViewTitle.setBackground(null);
        viewBinding.textViewCharacterOrJob.setBackground(null);
        viewBinding.textViewEpisodeAndYear.setBackground(null);
    }

    protected void renderDate(RowCardCreditBinding viewBinding, String date) {
        try {
            viewBinding.textViewEpisodeAndYear.setText(DateUtils.formatyyyy(DateUtils.toDate(date)));
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
            // viewBinding.textViewEpisodeAndYear.setVisibility(View.INVISIBLE);
        }
    }

    protected void loadPoster(RowCardCreditBinding viewBinding) {
        clearBackgrounds(viewBinding);
        viewBinding.imageViewPoster.loadBlur(coc.thumbnailTinyPoster());
        viewBinding.imageViewPoster.load(coc.thumbnailPoster());
    }
}
