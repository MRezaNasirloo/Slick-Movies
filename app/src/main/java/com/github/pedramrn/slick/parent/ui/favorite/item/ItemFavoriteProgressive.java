package com.github.pedramrn.slick.parent.ui.favorite.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.databinding.RowFavoriteMovieTvBinding;
import com.github.pedramrn.slick.parent.ui.Navigator;
import com.github.pedramrn.slick.parent.ui.home.Retryable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-26
 */

public class ItemFavoriteProgressive extends ItemFavorite {
    public ItemFavoriteProgressive(long uniqueId) {
        super(uniqueId, null, null);
    }

    @Override
    public void bind(RowFavoriteMovieTvBinding viewBinding, int position) {
        viewBinding.textViewFavoriteInfo.setText("    ");
        viewBinding.textViewFavoriteInfo.setBackgroundResource(R.drawable.line);
        viewBinding.imageViewFavoritePoster.load(R.drawable.rectangle);
    }

    @Override
    public boolean removableByTag(String tag) {
        return true;
    }

    @Override
    public void action(@NonNull Navigator navigator, Retryable retryable, @Nullable Object payload, int position, @NonNull View
            view) {
        //no-op
    }
}
