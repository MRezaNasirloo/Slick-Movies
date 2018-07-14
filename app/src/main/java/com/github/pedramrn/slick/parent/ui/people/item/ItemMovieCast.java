package com.github.pedramrn.slick.parent.ui.people.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowCardCreditBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.github.pedramrn.slick.parent.ui.people.model.CastOrCrewPersonDetails;
import com.github.pedramrn.slick.parent.util.DateUtils;
import com.xwray.groupie.Item;

import java.text.ParseException;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

@SuppressWarnings("WeakerAccess")
public class ItemMovieCast extends Item<RowCardCreditBinding> implements OnItemAction {

    protected final CastOrCrewPersonDetails coc;
    private final String transitionName;

    public CastOrCrewPersonDetails getCoc() {
        return coc;
    }

    public ItemMovieCast(CastOrCrewPersonDetails coc) {
        this.coc = coc;
        this.transitionName = ItemMovieCast.class.getSimpleName() + "_" + coc.id();
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
            viewBinding.textViewEpisodeAndYear.setVisibility(View.VISIBLE);
            viewBinding.textViewEpisodeAndYear.setText(DateUtils.format_yyyy(DateUtils.toDate(date)));
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
             viewBinding.textViewEpisodeAndYear.setVisibility(View.INVISIBLE);
        }
    }

    protected void loadPoster(RowCardCreditBinding viewBinding) {
        clearBackgrounds(viewBinding);
        viewBinding.imageViewPoster.setTransitionName(transitionName);
        viewBinding.imageViewPoster.loadBlur(coc.thumbnailTinyPoster());
        viewBinding.imageViewPoster.load(coc.thumbnailPoster());
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        navigator.navigateTo(ControllerDetails.newInstance(MovieSmall.create(coc), transitionName));
    }
}
